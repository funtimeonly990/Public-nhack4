package ryo.mrbubblegum.nhack4.lite.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MoverType;
import ryo.mrbubblegum.nhack4.impl.util.Util;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.system.setting.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FakePlayer
        extends Module {
    private static FakePlayer INSTANCE = new FakePlayer();
    public List<Integer> fakePlayerIdList = new ArrayList<Integer>();
    public Setting<Boolean> moving = this.register(new Setting("Moving", false));
    private EntityOtherPlayerMP otherPlayer;

    public FakePlayer() {
        super("FakePlayer", "average weakness player", Category.PLAYER, false, false, false);
        this.setInstance();
    }

    public static FakePlayer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakePlayer();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onTick() {
        if (otherPlayer != null) {
            Random random = new Random();
            otherPlayer.moveForward = Util.mc.player.moveForward + (random.nextInt(5) / 10F);
            otherPlayer.moveStrafing = Util.mc.player.moveStrafing + (random.nextInt(5) / 10F);
            if (moving.getValue()) travel(otherPlayer.moveStrafing, otherPlayer.moveVertical, otherPlayer.moveForward);
        }
    }

    public void travel(float strafe, float vertical, float forward) {
        double d0 = otherPlayer.posY;
        float f1 = 0.8F;
        float f2 = 0.02F;
        float f3 = (float) EnchantmentHelper.getDepthStriderModifier(otherPlayer);

        if (f3 > 3.0F) {
            f3 = 3.0F;
        }

        if (!otherPlayer.onGround) {
            f3 *= 0.5F;
        }

        if (f3 > 0.0F) {
            f1 += (0.54600006F - f1) * f3 / 3.0F;
            f2 += (otherPlayer.getAIMoveSpeed() - f2) * f3 / 4.0F;
        }

        otherPlayer.moveRelative(strafe, vertical, forward, f2);
        otherPlayer.move(MoverType.SELF, otherPlayer.motionX, otherPlayer.motionY, otherPlayer.motionZ);
        otherPlayer.motionX *= f1;
        otherPlayer.motionY *= 0.800000011920929D;
        otherPlayer.motionZ *= f1;

        if (!otherPlayer.hasNoGravity()) {
            otherPlayer.motionY -= 0.02D;
        }

        if (otherPlayer.collidedHorizontally && otherPlayer.isOffsetPositionInLiquid(otherPlayer.motionX, otherPlayer.motionY + 0.6000000238418579D - otherPlayer.posY + d0, otherPlayer.motionZ)) {
            otherPlayer.motionY = 0.30000001192092896D;
        }
    }

    @Override
    public void onEnable() {
        if (Util.mc.world == null || Util.mc.player == null) {
            toggle();
            return;
        }
        this.fakePlayerIdList = new ArrayList<Integer>();

        this.addFakePlayer(-100);
    }

    public void addFakePlayer(int entityId) {
        if (otherPlayer == null) {
            otherPlayer = new EntityOtherPlayerMP(Util.mc.world, new GameProfile(UUID.randomUUID(), "ryooooo"));
            otherPlayer.copyLocationAndAnglesFrom(Util.mc.player);
            otherPlayer.inventory.copyInventory(Util.mc.player.inventory);
        }
        Util.mc.world.addEntityToWorld(entityId, otherPlayer);
        this.fakePlayerIdList.add(entityId);

    }

    @Override
    public void onDisable() {
        for (int id : this.fakePlayerIdList) {
            FakePlayer.mc.world.removeEntityFromWorld(id);
        }
        if (otherPlayer != null) {
            Util.mc.world.removeEntity(otherPlayer);
            otherPlayer = null;
        }
    }
}