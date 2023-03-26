package ryo.mrbubblegum.nhack4.impl.util.shaders.impl.fill;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ryo.mrbubblegum.nhack4.impl.util.shaders.FramebufferShader;

import java.awt.*;
import java.util.HashMap;

public class FlowShader
        extends FramebufferShader {
    public static final FlowShader INSTANCE;

    static {
        INSTANCE = new FlowShader();
    }

    public float time;

    public FlowShader() {
        super("flow.frag");
    }

    public void startShader(float f, float f2, float f3, float f4, float f5, int n, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, int n2) {
        GL11.glPushMatrix();
        GL20.glUseProgram(this.program);
        if (this.uniformsMap == null) {
            this.uniformsMap = new HashMap();
            this.setupUniforms();
        }
        this.updateUniforms(f, f2, f3, f4, f5, n, f6, f7, f8, f9, f10, f11, f12, f13, n2);
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
        this.setupUniform("color");
        this.setupUniform("iterations");
        this.setupUniform("formuparam2");
        this.setupUniform("stepsize");
        this.setupUniform("volsteps");
        this.setupUniform("zoom");
        this.setupUniform("tile");
        this.setupUniform("distfading");
        this.setupUniform("saturation");
        this.setupUniform("fadeBol");
    }

    public void updateUniforms(float f, float f2, float f3, float f4, float f5, int n, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, int n2) {
        GL20.glUniform2f(this.getUniform("resolution"), (float) new ScaledResolution(this.mc).getScaledWidth() / f, (float) new ScaledResolution(this.mc).getScaledHeight() / f);
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform4f(this.getUniform("color"), f2, f3, f4, f5);
        GL20.glUniform1i(this.getUniform("iterations"), n);
        GL20.glUniform1f(this.getUniform("formuparam2"), f6);
        GL20.glUniform1i(this.getUniform("volsteps"), (int) f8);
        GL20.glUniform1f(this.getUniform("stepsize"), f9);
        GL20.glUniform1f(this.getUniform("zoom"), f7);
        GL20.glUniform1f(this.getUniform("tile"), f10);
        GL20.glUniform1f(this.getUniform("distfading"), f11);
        GL20.glUniform1f(this.getUniform("saturation"), f12);
        GL20.glUniform1i(this.getUniform("fadeBol"), n2);
    }

    public void update(double d) {
        this.time = (float) ((double) this.time + d);
    }

    public void stopDraw(Color color, float f, float f2, float f3, float f4, float f5, float f6, float f7, int n, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, int n2) {
        this.mc.gameSettings.entityShadows = this.entityShadows;
        this.framebuffer.unbindFramebuffer();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.mc.getFramebuffer().bindFramebuffer(true);
        this.radius = f;
        this.quality = f2;
        this.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        this.startShader(f3, f4, f5, f6, f7, n, f8, f9, f10, f11, f12, f13, f14, f15, n2);
        this.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(this.framebuffer);
        this.stopShader();
        this.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
}