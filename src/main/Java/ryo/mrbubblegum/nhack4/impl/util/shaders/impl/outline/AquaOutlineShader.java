package ryo.mrbubblegum.nhack4.impl.util.shaders.impl.outline;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ryo.mrbubblegum.nhack4.impl.util.shaders.FramebufferShader;

import java.awt.*;
import java.util.HashMap;

public final class AquaOutlineShader
        extends FramebufferShader {
    public static final AquaOutlineShader INSTANCE;

    static {
        INSTANCE = new AquaOutlineShader();
    }

    public float time;

    public AquaOutlineShader() {
        super("aquaOutline.frag");
        this.time = 0.0f;
    }

    public void update(double d) {
        this.time = (float) ((double) this.time + d);
    }

    public void startShader(Color color, float f, float f2, boolean bl, int n, float f3, int n2, double d) {
        GL11.glPushMatrix();
        GL20.glUseProgram(this.program);
        if (this.uniformsMap == null) {
            this.uniformsMap = new HashMap();
            this.setupUniforms();
        }
        this.updateUniforms(color, f, f2, bl, n, f3, n2, d);
    }

    public void updateUniforms(Color color, float f, float f2, boolean bl, int n, float f3, int n2, double d) {
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / (float) this.mc.displayWidth * (f * f2), 1.0f / (float) this.mc.displayHeight * (f * f2));
        GL20.glUniform1f(this.getUniform("divider"), 140.0f);
        GL20.glUniform1f(this.getUniform("radius"), f);
        GL20.glUniform1f(this.getUniform("maxSample"), 10.0f);
        GL20.glUniform1f(this.getUniform("alpha0"), bl ? -1.0f : (float) n / 255.0f);
        GL20.glUniform2f(this.getUniform("resolution"), (float) new ScaledResolution(this.mc).getScaledWidth() / f3, (float) new ScaledResolution(this.mc).getScaledHeight() / f3);
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform4f(this.getUniform("rgba"), (float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
        GL20.glUniform1i(this.getUniform("lines"), n2);
        GL20.glUniform1f(this.getUniform("tau"), (float) d);
    }

    public void stopDraw(Color color, float f, float f2, boolean bl, int n, float f3, int n2, double d) {
        this.mc.gameSettings.entityShadows = this.entityShadows;
        this.framebuffer.unbindFramebuffer();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.mc.getFramebuffer().bindFramebuffer(true);
        this.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        this.startShader(color, f, f2, bl, n, f3, n2, d);
        this.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(this.framebuffer);
        this.stopShader();
        this.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("divider");
        this.setupUniform("radius");
        this.setupUniform("maxSample");
        this.setupUniform("alpha0");
        this.setupUniform("resolution");
        this.setupUniform("time");
        this.setupUniform("rgba");
        this.setupUniform("lines");
        this.setupUniform("tau");
    }
}