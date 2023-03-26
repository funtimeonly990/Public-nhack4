package ryo.mrbubblegum.nhack4.lite.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.system.setting.Setting;
import ryo.mrbubblegum.nhack4.world.events.MoveEvent;

public class Sprint
        extends Module {
    private static Sprint INSTANCE = new Sprint();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.LEGIT));

    public Sprint() {
        super("Sprint", "rage sprint!!!!! :rage:", Module.Category.MOVEMENT, false, false, false);
        this.setInstance();
    }

    public static Sprint getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Sprint();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSprint(MoveEvent event) {
        if (event.getStage() == 1 && this.mode.getValue() == Mode.RAGE && (Sprint.mc.player.movementInput.moveForward != 0.0f || Sprint.mc.player.movementInput.moveStrafe != 0.0f)) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case RAGE: {
                if (!Sprint.mc.gameSettings.keyBindForward.isKeyDown() && !Sprint.mc.gameSettings.keyBindBack.isKeyDown() && !Sprint.mc.gameSettings.keyBindLeft.isKeyDown() && !Sprint.mc.gameSettings.keyBindRight.isKeyDown() || Sprint.mc.player.isSneaking() || Sprint.mc.player.collidedHorizontally || (float) Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f)
                    break;
                Sprint.mc.player.setSprinting(true);
                break;
            }
            case LEGIT: {
                if (!Sprint.mc.gameSettings.keyBindForward.isKeyDown() || Sprint.mc.player.isSneaking() || Sprint.mc.player.isHandActive() || Sprint.mc.player.collidedHorizontally || (float) Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f || Sprint.mc.currentScreen != null)
                    break;
                Sprint.mc.player.setSprinting(true);
            }
        }
    }

    @Override
    public void onDisable() {
        if (!Sprint.nullCheck()) {
            Sprint.mc.player.setSprinting(false);
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public enum Mode {
        LEGIT,
        RAGE

    }
}

