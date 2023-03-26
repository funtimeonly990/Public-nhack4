package ryo.mrbubblegum.nhack4.impl.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import ryo.mrbubblegum.nhack4.lite.render.PopChams;

import java.awt.*;

public class TotemPopChams {
    private static final Minecraft mc = Minecraft.getMinecraft();
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;

    public TotemPopChams(EntityOtherPlayerMP player, ModelPlayer playerModel, Long startTime, double alphaFill, double alphaLine) {
        MinecraftForge.EVENT_BUS.register(this);
        this.player = player;
        this.playerModel = playerModel;
        this.startTime = startTime;
        this.alphaFill = alphaFill;
        this.alphaLine = alphaFill;
    }

    public static void renderEntity(EntityLivingBase entity, ModelBase modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (mc.getRenderManager() == null) {
            return;
        }
        float partialTicks = mc.getRenderPartialTicks();
        double x = entity.posX - TotemPopChams.mc.getRenderManager().viewerPosX;
        double y = entity.posY - TotemPopChams.mc.getRenderManager().viewerPosY;
        double z = entity.posZ - TotemPopChams.mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            y -= 0.125;
        }
        float interpolateRotation = TotemPopChams.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        float interpolateRotation2 = TotemPopChams.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float rotationInterp = interpolateRotation2 - interpolateRotation;
        float renderPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        TotemPopChams.renderLivingAt(x, y, z);
        float f8 = TotemPopChams.handleRotationFloat(entity, partialTicks);
        TotemPopChams.prepareRotations(entity);
        float f9 = TotemPopChams.prepareScale(entity, scale);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, f8, entity.rotationYawHead, entity.rotationPitch, f9, entity);
        modelBase.render(entity, limbSwing, limbSwingAmount, f8, entity.rotationYawHead, entity.rotationPitch, f9);
        GlStateManager.popMatrix();
    }

    public static void prepareTranslate(EntityLivingBase entityIn, double x, double y, double z) {
        TotemPopChams.renderLivingAt(x - TotemPopChams.mc.getRenderManager().viewerPosX, y - TotemPopChams.mc.getRenderManager().viewerPosY, z - TotemPopChams.mc.getRenderManager().viewerPosZ);
    }

    public static void renderLivingAt(double x, double y, double z) {
        GlStateManager.translate((float) x, (float) y, (float) z);
    }

    public static float prepareScale(EntityLivingBase entity, float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale((double) scale + widthX, scale * entity.height, (double) scale + widthZ);
        float f = 0.0625f;
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return 0.0625f;
    }

    public static void prepareRotations(EntityLivingBase entityLivingBase) {
        GlStateManager.rotate(180.0f - entityLivingBase.rotationYaw, 0.0f, 1.0f, 0.0f);
    }

    public static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0f; f += 360.0f) {
        }
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevYawOffset + partialTicks * f;
    }

    public static Color newAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
    }

    public static float handleRotationFloat(EntityLivingBase livingBase, float partialTicks) {
        return 0.0f;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (this.player == null || TotemPopChams.mc.world == null || TotemPopChams.mc.player == null) {
            return;
        }
        GL11.glLineWidth(1.0f);
        Color lineColorS = new Color(PopChams.rL.getValue(), PopChams.gL.getValue(), PopChams.bL.getValue(), PopChams.aL.getValue());
        Color fillColorS = new Color(PopChams.rF.getValue(), PopChams.gF.getValue(), PopChams.bF.getValue(), PopChams.aF.getValue());
        int lineA = lineColorS.getAlpha();
        int fillA = fillColorS.getAlpha();
        long time = System.currentTimeMillis() - this.startTime - ((Number) PopChams.fadestart.getValue()).longValue();
        if (System.currentTimeMillis() - this.startTime > ((Number) PopChams.fadestart.getValue()).longValue()) {
            double normal = this.normalize(time, 0.0, ((Number) PopChams.fadetime.getValue()).doubleValue());
            normal = MathHelper.clamp(normal, 0.0, 1.0);
            normal = -normal + 1.0;
            lineA *= (int) normal;
            fillA *= (int) normal;
        }
        Color lineColor = TotemPopChams.newAlpha(lineColorS, lineA);
        Color fillColor = TotemPopChams.newAlpha(fillColorS, fillA);
        if (this.player != null && this.playerModel != null) {
            NordTessellator.prepareGL();
            GL11.glPushAttrib(1048575);
            GL11.glEnable(2881);
            GL11.glEnable(2848);
            if (this.alphaFill > 1.0) {
                this.alphaFill -= PopChams.fadetime.getValue().floatValue();
            }
            Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int) this.alphaFill);
            if (this.alphaLine > 1.0) {
                this.alphaLine -= PopChams.fadetime.getValue().floatValue();
            }
            Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int) this.alphaLine);
            TotemPopChams.glColor(fillFinal);
            GL11.glPolygonMode(1032, 6914);
            TotemPopChams.renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            TotemPopChams.glColor(outlineFinal);
            GL11.glPolygonMode(1032, 6913);
            TotemPopChams.renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            GL11.glPolygonMode(1032, 6914);
            GL11.glPopAttrib();
            NordTessellator.releaseGL();
        }
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }
}