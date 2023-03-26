package ryo.mrbubblegum.nhack4.impl.util.shaders.impl.fill;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ryo.mrbubblegum.nhack4.impl.util.shaders.FramebufferShader;

import java.awt.*;
import java.util.HashMap;

public class SmokeShader
        extends FramebufferShader {
    public static final SmokeShader INSTANCE;

    static {
        INSTANCE = new SmokeShader();
    }

    public float time;

    public SmokeShader() {
        super("smoke.frag");
    }

    public void startShader(float f, Color color, Color color2, Color color3, int n) {
        GL11.glPushMatrix();
        GL20.glUseProgram(this.program);
        if (this.uniformsMap == null) {
            this.uniformsMap = new HashMap();
            this.setupUniforms();
        }
        this.updateUniforms(f, color, color2, color3, n);
    }

    public void updateUniforms(float f, Color color, Color color2, Color color3, int n) {
        GL20.glUniform2f(this.getUniform("resolution"), (float) new ScaledResolution(this.mc).getScaledWidth() / f, (float) new ScaledResolution(this.mc).getScaledHeight() / f);
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform4f(this.getUniform("first"), (float) color.getRed() / 255.0f * 5.0f, (float) color.getGreen() / 255.0f * 5.0f, (float) color.getBlue() / 255.0f * 5.0f, (float) color.getAlpha() / 255.0f);
        GL20.glUniform3f(this.getUniform("second"), (float) color2.getRed() / 255.0f * 5.0f, (float) color2.getGreen() / 255.0f * 5.0f, (float) color2.getBlue() / 255.0f * 5.0f);
        GL20.glUniform3f(this.getUniform("third"), (float) color3.getRed() / 255.0f * 5.0f, (float) color3.getGreen() / 255.0f * 5.0f, (float) color3.getBlue() / 255.0f * 5.0f);
        GL20.glUniform1i(this.getUniform("oct"), n);
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
        this.setupUniform("first");
        this.setupUniform("second");
        this.setupUniform("third");
        this.setupUniform("oct");
    }

    public void update(double d) {
        this.time = (float) ((double) this.time + d);
    }

    public void stopDraw(Color color, float f, float f2, float f3, Color color2, Color color3, Color color4, int n) {
        this.mc.gameSettings.entityShadows = this.entityShadows;
        this.framebuffer.unbindFramebuffer();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.mc.getFramebuffer().bindFramebuffer(true);
        this.red = (float) color.getRed() / 255.0f;
        this.green = (float) color.getGreen() / 255.0f;
        this.blue = (float) color.getBlue() / 255.0f;
        this.alpha = (float) color.getAlpha() / 255.0f;
        this.radius = f;
        this.quality = f2;
        this.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        this.startShader(f3, color2, color3, color4, n);
        this.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(this.framebuffer);
        this.stopShader();
        this.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
}