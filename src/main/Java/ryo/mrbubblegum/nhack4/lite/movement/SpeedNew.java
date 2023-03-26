package ryo.mrbubblegum.nhack4.lite.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryo.mrbubblegum.nhack4.impl.util.EntityUtil;
import ryo.mrbubblegum.nhack4.impl.util.MovementUtil;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.loader.Loader;
import ryo.mrbubblegum.nhack4.system.setting.Setting;
import ryo.mrbubblegum.nhack4.world.events.MoveEvent;
import ryo.mrbubblegum.nhack4.world.events.UpdateWalkingPlayerEvent;

import java.util.Objects;

public class SpeedNew
        extends Module {
    private static SpeedNew instance;
    private final Setting<SpeedNewModes> mode = this.register(new Setting<SpeedNewModes>("Mode", SpeedNewModes.Custom));
    private final Setting<Float> yPortAirSpeed = this.register(new Setting<Float>("AirSpeed", Float.valueOf(0.35f), Float.valueOf(0.2f), Float.valueOf(5.0f), t -> this.mode.getValue().equals(SpeedNewModes.YPort)));
    private final Setting<Float> yPortGroundSpeed = this.register(new Setting<Float>("GroundSpeed", Float.valueOf(0.35f), Float.valueOf(0.2f), Float.valueOf(5.0f), t -> this.mode.getValue().equals(SpeedNewModes.YPort)));
    private final Setting<Float> yPortJumpMotionY = this.register(new Setting<Float>("JumpMotionY", Float.valueOf(0.42f), Float.valueOf(0.0f), Float.valueOf(4.0f), t -> this.mode.getValue().equals(SpeedNewModes.YPort)));
    private final Setting<Float> yPortFallSpeed = this.register(new Setting<Float>("FallSpeed", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(4.0f), t -> this.mode.getValue().equals(SpeedNewModes.YPort)));
    private final Setting<Boolean> yPortTimerSpeed = this.register(new Setting<Boolean>("Timer", Boolean.valueOf(false), t -> this.mode.getValue().equals(SpeedNewModes.YPort)));
    private final Setting<Float> yPortTimerSpeedVal = this.register(new Setting<Float>("TimerSpeed", Float.valueOf(1.8f), Float.valueOf(1.0f), Float.valueOf(5.0f), t -> this.yPortTimerSpeed.getValue() && this.mode.getValue().equals(SpeedNewModes.YPort)));

    private final Setting<Boolean> customStrafe = this.register(new Setting<Boolean>("CustomStrafe", Boolean.valueOf(true), t -> this.mode.getValue().equals(SpeedNewModes.Custom)));
    private final Setting<Float> airSpeed = this.register(new Setting<Float>("AirSpeed", Float.valueOf(0.35f), Float.valueOf(0.2f), Float.valueOf(5.0f), t -> this.customStrafe.getValue() && this.mode.getValue().equals(SpeedNewModes.Custom)));
    private final Setting<Float> onGroundSpeed = this.register(new Setting<Float>("OnGroundSpeed", Float.valueOf(0.35f), Float.valueOf(0.2f), Float.valueOf(5.0f), t -> this.mode.getValue().equals(SpeedNewModes.Custom)));
    private final Setting<Boolean> autoJump = this.register(new Setting<Boolean>("AutoJump", Boolean.valueOf(true), t -> this.mode.getValue().equals(SpeedNewModes.Custom)));
    private final Setting<Float> jumpMotionY = this.register(new Setting<Float>("JumpMotionY", Float.valueOf(0.42f), Float.valueOf(0.0f), Float.valueOf(4.0f), t -> this.autoJump.getValue() && this.mode.getValue().equals(SpeedNewModes.Custom)));
    private final Setting<Boolean> fallModify = this.register(new Setting<Boolean>("FallModify", Boolean.valueOf(false), t -> this.autoJump.getValue() && this.mode.getValue().equals(SpeedNewModes.Custom)));
    private final Setting<Float> fallSpeed = this.register(new Setting<Float>("FallSpeed", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), t -> this.fallModify.getValue() && this.autoJump.getValue() && this.mode.getValue().equals(SpeedNewModes.Custom)));
    private final Setting<Boolean> timerSpeed = this.register(new Setting<Boolean>("Timer", Boolean.valueOf(false), t -> this.mode.getValue().equals(SpeedNewModes.Custom)));
    private final Setting<Float> timerSpeedVal = this.register(new Setting<Float>("TimerSpeed", Float.valueOf(1.8f), Float.valueOf(1.0f), Float.valueOf(5.0f), t -> this.timerSpeed.getValue() && this.mode.getValue().equals(SpeedNewModes.Custom)));
    private final Setting<Boolean> resetXZ = this.register(new Setting<Boolean>("ResetXZ", Boolean.valueOf(false), t -> this.mode.getValue().equals(SpeedNewModes.Custom)));
    private final Setting<Boolean> resetY = this.register(new Setting<Boolean>("ResetY", Boolean.valueOf(false), t -> this.mode.getValue().equals(SpeedNewModes.Custom)));
    int stage;
    private double lastDist;
    private double moveSpeedNew;

    public SpeedNew() {
        super("Speed", "speed module!!", Module.Category.MOVEMENT, false, false, false);
        instance = this;
    }

    public static SpeedNew getInstance() {
        if (instance == null) {
            instance = new SpeedNew();
        }
        return instance;
    }

    @SubscribeEvent
    public void onStrafe(MoveEvent event) {
        if (Strafe.fullNullCheck()) {
            return;
        }
        if (Strafe.mc.player.isInWater() || Strafe.mc.player.isInLava()) {
            return;
        }
        if (Strafe.mc.player.onGround) {
            this.stage = 2;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                double motionY = 0.40123128;
                if (!Strafe.mc.player.onGround || !Strafe.mc.gameSettings.keyBindJump.isKeyDown()) {
                    break;
                }
                if (Strafe.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    motionY += (float) (Strafe.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                }
                Strafe.mc.player.motionY = motionY;
                event.setY(Strafe.mc.player.motionY);
                this.moveSpeedNew *= 2.149;
                break;
            }
            case 3: {
                this.moveSpeedNew = this.lastDist - 0.795 * (this.lastDist - this.getBaseMoveSpeedNew());
                break;
            }
            default: {
                if ((Strafe.mc.world.getCollisionBoxes(Strafe.mc.player, Strafe.mc.player.getEntityBoundingBox().offset(0.0, Strafe.mc.player.motionY, 0.0)).size() > 0 || Strafe.mc.player.collidedVertically) && this.stage > 0) {
                    this.stage = Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f ? 1 : 0;
                }
                this.moveSpeedNew = this.lastDist - this.lastDist / 159.0;
            }
        }
        this.moveSpeedNew = !Strafe.mc.gameSettings.keyBindJump.isKeyDown() && Strafe.mc.player.onGround ? this.getBaseMoveSpeedNew() : Math.max(this.moveSpeedNew, this.getBaseMoveSpeedNew());
        double n = Strafe.mc.player.movementInput.moveForward;
        double n2 = Strafe.mc.player.movementInput.moveStrafe;
        double n3 = Strafe.mc.player.rotationYaw;
        if (n == 0.0 && n2 == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else if (n != 0.0 && n2 != 0.0) {
            n *= Math.sin(0.7853981633974483);
            n2 *= Math.cos(0.7853981633974483);
        }
        double n4 = 0.99;
        event.setX((n * this.moveSpeedNew * -Math.sin(Math.toRadians(n3)) + n2 * this.moveSpeedNew * Math.cos(Math.toRadians(n3))) * n4);
        event.setZ((n * this.moveSpeedNew * Math.cos(Math.toRadians(n3)) - n2 * this.moveSpeedNew * -Math.sin(Math.toRadians(n3))) * n4);
        ++this.stage;
        event.setCanceled(true);
    }

    public double getBaseMoveSpeedNew() {
        double n = 0.2873;
        if (!Strafe.mc.player.isPotionActive(MobEffects.SPEED)) {
            return n;
        }
        return n *= 1.0 + 0.2 * (double) (Objects.requireNonNull(Strafe.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1);
    }

    @SubscribeEvent
    public void onMotion(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1 || Strafe.fullNullCheck()) {
            return;
        }
        switch (this.mode.getValue()) {
            case Custom: {
                if (MovementUtil.isMoving(SpeedNew.mc.player)) {
                    if (SpeedNew.mc.player.onGround) {
                        EntityUtil.moveEntityStrafe(this.onGroundSpeed.getValue().floatValue(), SpeedNew.mc.player);
                        if (this.autoJump.getValue().booleanValue()) {
                            SpeedNew.mc.player.motionY = this.jumpMotionY.getValue().floatValue();
                            break;
                        }
                    } else {
                        if (this.fallModify.getValue().booleanValue()) {
                            SpeedNew.mc.player.motionY = -this.fallSpeed.getValue().floatValue();
                        }
                    }
                    if (this.customStrafe.getValue().booleanValue()) {
                        EntityUtil.moveEntityStrafe(this.airSpeed.getValue().floatValue(), SpeedNew.mc.player);
                        break;
                    }
                    EntityUtil.moveEntityStrafe(Math.sqrt(SpeedNew.mc.player.motionX * SpeedNew.mc.player.motionX + SpeedNew.mc.player.motionY * SpeedNew.mc.player.motionY + SpeedNew.mc.player.motionZ * SpeedNew.mc.player.motionZ), SpeedNew.mc.player);
                    break;
                }
                if (this.timerSpeed.getValue().booleanValue() == Boolean.TRUE) {
                    Loader.timerManager.setTimer(this.timerSpeedVal.getValue().floatValue());
                } else {
                    Loader.timerManager.reset();
                }
                SpeedNew.mc.player.motionX = SpeedNew.mc.player.motionZ = 0.0;
                break;
            }
            case YPort: {
                if (!MovementUtil.isMoving(Speed.mc.player) || Speed.mc.player.collidedHorizontally) {
                    return;
                }
                if (this.yPortTimerSpeed.getValue().booleanValue()) {
                    Loader.timerManager.setTimer(this.yPortTimerSpeedVal.getValue().floatValue());
                } else {
                    Loader.timerManager.reset();
                }
                if (Speed.mc.player.onGround) {
                    SpeedNew.mc.player.motionY = this.yPortJumpMotionY.getValue().floatValue();
                    EntityUtil.moveEntityStrafe(this.yPortGroundSpeed.getValue().floatValue(), SpeedNew.mc.player);
                } else {
                    Speed.mc.player.motionY = -this.yPortFallSpeed.getValue().floatValue();
                }
                if (!Speed.mc.player.onGround) {
                    SpeedNew.mc.player.motionY = this.yPortJumpMotionY.getValue().floatValue();
                    EntityUtil.moveEntityStrafe(this.yPortAirSpeed.getValue().floatValue(), SpeedNew.mc.player);
                }
            }
        }
    }

    @Override
    public void onTick() {
        switch (this.mode.getValue()) {
            case Custom: {
                if (MovementUtil.isMoving(SpeedNew.mc.player)) {
                    if (SpeedNew.mc.player.onGround) {
                        EntityUtil.moveEntityStrafe(this.onGroundSpeed.getValue().floatValue(), SpeedNew.mc.player);
                        if (this.autoJump.getValue().booleanValue()) {
                            SpeedNew.mc.player.motionY = this.jumpMotionY.getValue().floatValue();
                            break;
                        }
                    } else {
                        if (this.fallModify.getValue().booleanValue()) {
                            SpeedNew.mc.player.motionY = -this.fallSpeed.getValue().floatValue();
                        }
                    }
                    if (this.customStrafe.getValue().booleanValue()) {
                        EntityUtil.moveEntityStrafe(this.airSpeed.getValue().floatValue(), SpeedNew.mc.player);
                        break;
                    }
                    EntityUtil.moveEntityStrafe(Math.sqrt(SpeedNew.mc.player.motionX * SpeedNew.mc.player.motionX + SpeedNew.mc.player.motionY * SpeedNew.mc.player.motionY + SpeedNew.mc.player.motionZ * SpeedNew.mc.player.motionZ), SpeedNew.mc.player);
                    break;
                }
                if (this.timerSpeed.getValue().booleanValue() == Boolean.TRUE) {
                    Loader.timerManager.setTimer(this.timerSpeedVal.getValue().floatValue());
                } else {
                    Loader.timerManager.reset();
                }
                SpeedNew.mc.player.motionX = SpeedNew.mc.player.motionZ = 0.0;
                break;
            }
            case YPort: {
                if (!MovementUtil.isMoving(Speed.mc.player) || Speed.mc.player.collidedHorizontally) {
                    return;
                }
                if (this.yPortTimerSpeed.getValue().booleanValue()) {
                    Loader.timerManager.setTimer(this.yPortTimerSpeedVal.getValue().floatValue());
                } else {
                    Loader.timerManager.reset();
                }
                if (Speed.mc.player.onGround) {
                    SpeedNew.mc.player.motionY = this.yPortJumpMotionY.getValue().floatValue();
                    EntityUtil.moveEntityStrafe(this.yPortGroundSpeed.getValue().floatValue(), SpeedNew.mc.player);
                } else {
                    Speed.mc.player.motionY = -this.yPortFallSpeed.getValue().floatValue();
                }
                if (!Speed.mc.player.onGround) {
                    EntityUtil.moveEntityStrafe(this.yPortAirSpeed.getValue().floatValue(), SpeedNew.mc.player);
                }
            }
        }
    }

    @Override
    public void onEnable() {
        if (this.timerSpeed.getValue().booleanValue()) {
            Loader.timerManager.setTimer(this.timerSpeedVal.getValue().floatValue());
        }
        if (this.resetXZ.getValue().booleanValue()) {
            SpeedNew.mc.player.motionX = SpeedNew.mc.player.motionZ = 0.0;
        }
        if (this.resetY.getValue().booleanValue()) {
            SpeedNew.mc.player.motionX = 0.0;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Loader.timerManager.reset();
        super.onDisable();
    }

    public enum SpeedNewModes {
        Custom,
        YPort
    }
}
