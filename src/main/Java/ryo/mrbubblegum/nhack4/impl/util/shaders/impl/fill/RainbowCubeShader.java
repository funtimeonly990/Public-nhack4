package ryo.mrbubblegum.nhack4.impl.util.shaders.impl.fill;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ryo.mrbubblegum.nhack4.impl.util.shaders.FramebufferShader;

import java.awt.*;
import java.util.HashMap;

public class RainbowCubeShader
        extends FramebufferShader {
    public static final RainbowCubeShader INSTANCE;

    static {
        INSTANCE = new RainbowCubeShader();
    }

    public float time;

    public RainbowCubeShader() {
        super("rainbowCube.frag");
    }

    public void updateUniforms(float f, Color color, int n, int n2, int n3, int n4) {
        GL20.glUniform2f(this.getUniform("resolution"), (float) new ScaledResolution(this.mc).getScaledWidth() / f, (float) new ScaledResolution(this.mc).getScaledHeight() / f);
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform1f(this.getUniform("alpha"), (float) color.getAlpha() / 255.0f);
        GL20.glUniform1f(this.getUniform("WAVELENGTH"), (float) n);
        GL20.glUniform1i(this.getUniform("R"), color.getRed());
        GL20.glUniform1i(this.getUniform("G"), color.getGreen());
        GL20.glUniform1i(this.getUniform("B"), color.getBlue());
        GL20.glUniform1i(this.getUniform("RSTART"), n2);
        GL20.glUniform1i(this.getUniform("GSTART"), n3);
        GL20.glUniform1i(this.getUniform("BSTART"), n4);
    }

    public void update(double d) {
        this.time = (float) ((double) this.time + d);
    }

    public void stopDraw(Color color, float f, float f2, float f3, Color color2, int n, int n2, int n3, int n4) {
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
        this.startShader(f3, color2, n, n2, n3, n4);
        this.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(this.framebuffer);
        this.stopShader();
        this.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
        this.setupUniform("alpha");
        this.setupUniform("WAVELENGTH");
        this.setupUniform("R");
        this.setupUniform("G");
        this.setupUniform("B");
        this.setupUniform("RSTART");
        this.setupUniform("GSTART");
        this.setupUniform("BSTART");
    }

    public void startShader(float f, Color color, int n, int n2, int n3, int n4) {
        GL11.glPushMatrix();
        GL20.glUseProgram(this.program);
        if (this.uniformsMap == null) {
            this.uniformsMap = new HashMap();
            this.setupUniforms();
        }
        this.updateUniforms(f, color, n, n2, n3, n4);
    }
}