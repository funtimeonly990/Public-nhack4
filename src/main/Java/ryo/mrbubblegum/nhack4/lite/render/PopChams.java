package ryo.mrbubblegum.nhack4.lite.render;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import ryo.mrbubblegum.nhack4.impl.util.NordTessellator;
import ryo.mrbubblegum.nhack4.impl.util.TotemPopChams;
import ryo.mrbubblegum.nhack4.impl.util.Util;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.system.setting.Setting;
import ryo.mrbubblegum.nhack4.world.events.PacketEvent;

import java.awt.*;

public class PopChams
        extends Module {
    public static Setting<Boolean> self;
    public static Setting<Boolean> elevator;
    public static Setting<Integer> rL;
    public static Setting<Integer> gL;
    public static Setting<Integer> bL;
    public static Setting<Integer> aL;
    public static Setting<Integer> rF;
    public static Setting<Integer> gF;
    public static Setting<Integer> bF;
    public static Setting<Integer> aF;
    public static Setting<Integer> fadestart;
    public static Setting<Float> fadetime;
    public static Setting<Boolean> onlyOneEsp;
    public static Setting<ElevatorMode> elevatorMode;
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;

    public PopChams() {
        super("PopChams", "Renders a Glowing fakeplayer in the exact location where your enemy popped", Module.Category.RENDER, true, false, false);
        self = this.register(new Setting<Boolean>("Render Own Pops", true));
        elevator = this.register(new Setting<Boolean>("Travel", true));
        elevatorMode = this.register(new Setting<ElevatorMode>("Elevator", ElevatorMode.UP, v -> elevator.getValue()));
        rL = this.register(new Setting<Integer>("Outline Red", 135, 0, 255));
        bL = this.register(new Setting<Integer>("Outline Green", 0, 0, 255));
        gL = this.register(new Setting<Integer>("Outline Blue", 255, 0, 255));
        aL = this.register(new Setting<Integer>("Outline Alpha", 255, 0, 255));
        rF = this.register(new Setting<Integer>("Fill Red", 135, 0, 255));
        bF = this.register(new Setting<Integer>("Fill Green", 0, 0, 255));
        gF = this.register(new Setting<Integer>("Fill Blue", 255, 0, 255));
        aF = this.register(new Setting<Integer>("Fill Alpha", 140, 0, 255));
        fadestart = this.register(new Setting<Integer>("Fade Start", 0, 0, 255));
        fadetime = this.register(new Setting<Float>("Fade Time", Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(2.0f)));
        onlyOneEsp = this.register(new Setting<Boolean>("Only Render One", true));
    }

    public static void renderEntity(EntityLivingBase entity, ModelBase modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, int scale) {
        if (Util.mc.getRenderManager() == null) {
            return;
        }
        float partialTicks = Util.mc.getRenderPartialTicks();
        double x = entity.posX - PopChams.mc.getRenderManager().viewerPosX;
        double y = entity.posY - PopChams.mc.getRenderManager().viewerPosY;
        double z = entity.posZ - PopChams.mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            y -= 0.125;
        }
        float interpolateRotation = PopChams.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        float interpolateRotation2 = PopChams.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float rotationInterp = interpolateRotation2 - interpolateRotation;
        float renderPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        PopChams.renderLivingAt(x, y, z);
        float f8 = PopChams.handleRotationFloat(entity, partialTicks);
        PopChams.prepareRotations(entity);
        float f9 = PopChams.prepareScale(entity, scale);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9, entity);
        modelBase.render(entity, limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9);
        GlStateManager.popMatrix();
    }

    public static void prepareTranslate(EntityLivingBase entityIn, double x, double y, double z) {
        PopChams.renderLivingAt(x - PopChams.mc.getRenderManager().viewerPosX, y - PopChams.mc.getRenderManager().viewerPosY, z - PopChams.mc.getRenderManager().viewerPosZ);
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
    public void onUpdate(PacketEvent.Receive event) {
        SPacketEntityStatus packet;
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = event.getPacket()).getOpCode() == 35 && packet.getEntity(PopChams.mc.world) != null && (self.getValue().booleanValue() || packet.getEntity(PopChams.mc.world).getEntityId() != PopChams.mc.player.getEntityId())) {
            GameProfile profile = new GameProfile(PopChams.mc.player.getUniqueID(), "");
            this.player = new EntityOtherPlayerMP(PopChams.mc.world, profile);
            this.player.copyLocationAndAnglesFrom(packet.getEntity(PopChams.mc.world));
            this.playerModel = new ModelPlayer(0.0f, false);
            this.startTime = System.currentTimeMillis();
            this.playerModel.bipedHead.showModel = false;
            this.playerModel.bipedBody.showModel = false;
            this.playerModel.bipedLeftArmwear.showModel = false;
            this.playerModel.bipedLeftLegwear.showModel = false;
            this.playerModel.bipedRightArmwear.showModel = false;
            this.playerModel.bipedRightLegwear.showModel = false;
            this.alphaFill = aF.getValue().intValue();
            this.alphaLine = aL.getValue().intValue();
            if (!onlyOneEsp.getValue().booleanValue()) {
                TotemPopChams totemPopChams = new TotemPopChams(this.player, this.playerModel, this.startTime, this.alphaFill, this.alphaLine);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (onlyOneEsp.getValue().booleanValue()) {
            if (this.player == null || PopChams.mc.world == null || PopChams.mc.player == null) {
                return;
            }
            if (elevator.getValue().booleanValue()) {
                if (elevatorMode.getValue() == ElevatorMode.UP) {
                    this.player.posY += 0.05f * event.getPartialTicks();
                } else if (elevatorMode.getValue() == ElevatorMode.DOWN) {
                    this.player.posY -= 0.05f * event.getPartialTicks();
                }
            }
            GL11.glLineWidth(1.0f);
            Color lineColorS = new Color(rL.getValue(), bL.getValue(), gL.getValue(), aL.getValue());
            Color fillColorS = new Color(rF.getValue(), bF.getValue(), gF.getValue(), aF.getValue());
            int lineA = lineColorS.getAlpha();
            int fillA = fillColorS.getAlpha();
            long time = System.currentTimeMillis() - this.startTime - ((Number) fadestart.getValue()).longValue();
            if (System.currentTimeMillis() - this.startTime > ((Number) fadestart.getValue()).longValue()) {
                double normal = this.normalize(time, 0.0, ((Number) fadetime.getValue()).doubleValue());
                normal = MathHelper.clamp(normal, 0.0, 1.0);
                normal = -normal + 1.0;
                lineA *= (int) normal;
                fillA *= (int) normal;
            }
            Color lineColor = PopChams.newAlpha(lineColorS, lineA);
            Color fillColor = PopChams.newAlpha(fillColorS, fillA);
            if (this.player != null && this.playerModel != null) {
                NordTessellator.prepareGL();
                GL11.glPushAttrib(1048575);
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                if (this.alphaFill > 1.0) {
                    this.alphaFill -= fadetime.getValue().floatValue();
                }
                Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int) this.alphaFill);
                if (this.alphaLine > 1.0) {
                    this.alphaLine -= fadetime.getValue().floatValue();
                }
                Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int) this.alphaLine);
                PopChams.glColor(fillFinal);
                GL11.glPolygonMode(1032, 6914);
                PopChams.renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                PopChams.glColor(outlineFinal);
                GL11.glPolygonMode(1032, 6913);
                PopChams.renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                NordTessellator.releaseGL();
            }
        }
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public enum ElevatorMode {
        UP,
        DOWN
    }
}