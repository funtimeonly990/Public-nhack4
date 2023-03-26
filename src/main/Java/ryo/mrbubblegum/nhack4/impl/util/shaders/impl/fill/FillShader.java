package ryo.mrbubblegum.nhack4.impl.util.shaders.impl.fill;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ryo.mrbubblegum.nhack4.impl.util.shaders.FramebufferShader;

import java.awt.*;
import java.util.HashMap;

public class FillShader
        extends FramebufferShader {
    public static final FillShader INSTANCE;

    static {
        INSTANCE = new FillShader();
    }

    public float time;

    public FillShader() {
        super("fill.frag");
    }

    public void update(double d) {
        this.time = (float) ((double) this.time + d);
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("color");
    }

    public void startShader(float f, float f2, float f3, float f4) {
        GL11.glPushMatrix();
        GL20.glUseProgram(this.program);
        if (this.uniformsMap == null) {
            this.uniformsMap = new HashMap();
            this.setupUniforms();
        }
        this.updateUniforms(f, f2, f3, f4);
    }

    public void stopDraw(Color color) {
        this.mc.gameSettings.entityShadows = this.entityShadows;
        this.framebuffer.unbindFramebuffer();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.mc.getFramebuffer().bindFramebuffer(true);
        this.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        this.startShader((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
        this.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(this.framebuffer);
        this.stopShader();
        this.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public void updateUniforms(float f, float f2, float f3, float f4) {
        GL20.glUniform4f(this.getUniform("color"), f, f2, f3, f4);
    }
}