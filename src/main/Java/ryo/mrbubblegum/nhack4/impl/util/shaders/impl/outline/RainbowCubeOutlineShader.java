package ryo.mrbubblegum.nhack4.impl.util.shaders.impl.outline;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ryo.mrbubblegum.nhack4.impl.util.shaders.FramebufferShader;

import java.awt.*;
import java.util.HashMap;

public final class RainbowCubeOutlineShader
        extends FramebufferShader {
    public static final RainbowCubeOutlineShader INSTANCE;

    static {
        INSTANCE = new RainbowCubeOutlineShader();
    }

    public float time;

    public RainbowCubeOutlineShader() {
        super("rainbowCubeOutline.frag");
        this.time = 0.0f;
    }

    public void startShader(Color color, float f, float f2, boolean bl, int n, float f3, Color color2, int n2, int n3, int n4, int n5) {
        GL11.glPushMatrix();
        GL20.glUseProgram(this.program);
        if (this.uniformsMap == null) {
            this.uniformsMap = new HashMap();
            this.setupUniforms();
        }
        this.updateUniforms(color, f, f2, bl, n, f3, color2, n2, n3, n4, n5);
    }

    public void update(double d) {
        this.time = (float) ((double) this.time + d);
    }

    public void updateUniforms(Color color, float f, float f2, boolean bl, int n, float f3, Color color2, int n2, int n3, int n4, int n5) {
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / (float) this.mc.displayWidth * (f * f2), 1.0f / (float) this.mc.displayHeight * (f * f2));
        GL20.glUniform1f(this.getUniform("divider"), 140.0f);
        GL20.glUniform1f(this.getUniform("radius"), f);
        GL20.glUniform1f(this.getUniform("maxSample"), 10.0f);
        GL20.glUniform1f(this.getUniform("alpha0"), bl ? -1.0f : (float) n / 255.0f);
        GL20.glUniform2f(this.getUniform("resolution"), (float) new ScaledResolution(this.mc).getScaledWidth() / f3, (float) new ScaledResolution(this.mc).getScaledHeight() / f3);
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform1f(this.getUniform("alpha"), (float) color2.getAlpha() / 255.0f);
        GL20.glUniform1f(this.getUniform("WAVELENGTH"), (float) n2);
        GL20.glUniform1i(this.getUniform("R"), color2.getRed());
        GL20.glUniform1i(this.getUniform("G"), color2.getGreen());
        GL20.glUniform1i(this.getUniform("B"), color2.getBlue());
        GL20.glUniform1i(this.getUniform("RSTART"), n3);
        GL20.glUniform1i(this.getUniform("GSTART"), n4);
        GL20.glUniform1i(this.getUniform("BSTART"), n5);
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("divider");
        this.setupUniform("radius");
        this.setupUniform("maxSample");
        this.setupUniform("alpha0");
        this.setupUniform("resolution");
        this.setupUniform("time");
        this.setupUniform("WAVELENGTH");
        this.setupUniform("R");
        this.setupUniform("G");
        this.setupUniform("B");
        this.setupUniform("RSTART");
        this.setupUniform("GSTART");
        this.setupUniform("BSTART");
        this.setupUniform("alpha");
    }

    public void stopDraw(Color color, float f, float f2, boolean bl, int n, float f3, Color color2, int n2, int n3, int n4, int n5) {
        this.mc.gameSettings.entityShadows = this.entityShadows;
        this.framebuffer.unbindFramebuffer();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.mc.getFramebuffer().bindFramebuffer(true);
        this.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        this.startShader(color, f, f2, bl, n, f3, color2, n2, n3, n4, n5);
        this.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(this.framebuffer);
        this.stopShader();
        this.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
}