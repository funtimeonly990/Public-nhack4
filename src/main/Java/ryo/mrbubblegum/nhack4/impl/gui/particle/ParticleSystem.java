package ryo.mrbubblegum.nhack4.impl.gui.particle;

import net.minecraft.client.gui.ScaledResolution;
import ryo.mrbubblegum.nhack4.impl.util.ColorUtil;
import ryo.mrbubblegum.nhack4.impl.util.RenderUtil;
import ryo.mrbubblegum.nhack4.lite.client.ClickGui;

import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;

public final class ParticleSystem {
    private final int PARTS = 200;
    private final Particle[] particles = new Particle[200];
    private ScaledResolution scaledResolution;

    public ParticleSystem(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
        for (int i = 0; i < 200; ++i) {
            this.particles[i] = new Particle(new Vector2f((float) (Math.random() * (double) scaledResolution.getScaledWidth()), (float) (Math.random() * (double) scaledResolution.getScaledHeight())));
        }
    }

    public static double map(double value, double a, double b, double c, double d) {
        value = (value - a) / (b - a);
        return c + value * (d - c);
    }

    public void update() {
        for (int i = 0; i < 200; ++i) {
            Particle particle = this.particles[i];
            if (this.scaledResolution != null) {
                boolean isOffScreenY;
                boolean isOffScreenX = particle.getPos().x > (float) this.scaledResolution.getScaledWidth() || particle.getPos().x < 0.0f;
                boolean bl = isOffScreenY = particle.getPos().y > (float) this.scaledResolution.getScaledHeight() || particle.getPos().y < 0.0f;
                if (isOffScreenX || isOffScreenY) {
                    particle.respawn(this.scaledResolution);
                }
            }
            particle.update();
        }
    }

    public void render(int mouseX, int mouseY) {
        if (!ClickGui.getInstance().particles.getValue().booleanValue()) {
            return;
        }
        for (int i = 0; i < 200; ++i) {
            Particle particle = this.particles[i];
            for (int j = 1; j < 200; ++j) {
                int lineAlpha;
                if (i == j) continue;
                Particle otherParticle = this.particles[j];
                Vector2f diffPos = new Vector2f(particle.getPos());
                diffPos.sub(otherParticle.getPos());
                float diff = diffPos.length();
                int distance = ClickGui.getInstance().particleLength.getValue() / (this.scaledResolution.getScaleFactor() <= 1 ? 3 : this.scaledResolution.getScaleFactor());
                if (!(diff < (float) distance) || (lineAlpha = (int) ParticleSystem.map(diff, distance, 0.0, 0.0, 127.0)) <= 8)
                    continue;
                RenderUtil.drawLine(particle.getPos().x + particle.getSize() / 2.0f, particle.getPos().y + particle.getSize() / 2.0f, otherParticle.getPos().x + otherParticle.getSize() / 2.0f, otherParticle.getPos().y + otherParticle.getSize() / 2.0f, 1.0f, Particle.changeAlpha(ColorUtil.toRGBA(ClickGui.getInstance().particlered.getValue(), ClickGui.getInstance().particlegreen.getValue(), ClickGui.getInstance().particleblue.getValue()), lineAlpha));
            }
            particle.render(mouseX, mouseY);
        }
    }

    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

    public void setScaledResolution(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
}