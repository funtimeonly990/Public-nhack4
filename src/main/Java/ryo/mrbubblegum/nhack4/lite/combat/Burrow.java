package ryo.mrbubblegum.nhack4.lite.combat;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import ryo.mrbubblegum.nhack4.impl.util.BurrowUtil;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.system.command.Command;
import ryo.mrbubblegum.nhack4.system.setting.Setting;

public class Burrow
        extends Module {
    private final Setting<Integer> offset = this.register(new Setting<Integer>("Offset", 3, -5, 5));
    private final Setting<Boolean> ground = this.register(new Setting<Boolean>("GroundCheck", true));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Boolean> center = this.register(new Setting<Boolean>("Center", true));
    private final Setting<Boolean> echest = this.register(new Setting<Boolean>("UseEchest", false));
    private final Setting<Boolean> anvil = this.register(new Setting<Boolean>("Beacon", false));
    private final Setting<Boolean> web = this.register(new Setting<Boolean>("Chest", false));
    private final Setting<Boolean> skull = this.register(new Setting<Boolean>("UseSkull", false));

    private BlockPos originalPos;
    private int oldSlot = -1;

    public Burrow() {
        super("SelfBlock", "no skill require", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.originalPos = new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ);
        if (Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) || this.intersectsWithEntity(this.originalPos)) {
            this.toggle();
            return;
        }
        if (this.center.getValue().booleanValue()) {
            double x = Burrow.mc.player.posX - Math.floor(Burrow.mc.player.posX);
            double z = Burrow.mc.player.posZ - Math.floor(Burrow.mc.player.posZ);
            if (x <= 0.3 || x >= 0.7) {
                double d = x = x > 0.5 ? 0.69 : 0.31;
            }
            if (z < 0.3 || z > 0.7) {
                z = z > 0.5 ? 0.69 : 0.31;
            }
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(Burrow.mc.player.posX) + x, Burrow.mc.player.posY, Math.floor(Burrow.mc.player.posZ) + z, Burrow.mc.player.onGround));
            Burrow.mc.player.setPosition(Math.floor(Burrow.mc.player.posX) + x, Burrow.mc.player.posY, Math.floor(Burrow.mc.player.posZ) + z);
        }
        this.oldSlot = Burrow.mc.player.inventory.currentItem;
    }

    @Override
    public void onUpdate() {
        if (this.ground.getValue().booleanValue() && !Burrow.mc.player.onGround) {
            this.toggle();
            return;
        }

        if (this.anvil.getValue().booleanValue() && BurrowUtil.findHotbarBlock(BlockBeacon.class) != -1) {
            BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockBeacon.class));
        } else if (this.echest.getValue() ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) != -1 : BurrowUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            BurrowUtil.switchToSlot(this.echest.getValue() ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) : BurrowUtil.findHotbarBlock(BlockObsidian.class));
            if (this.web.getValue().booleanValue() && BurrowUtil.findHotbarBlock(BlockChest.class) != -1) ;
            BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockChest.class));
        } else if (this.echest.getValue() ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) != -1 : BurrowUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            BurrowUtil.switchToSlot(this.echest.getValue() ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) : BurrowUtil.findHotbarBlock(BlockObsidian.class));
            if (this.skull.getValue().booleanValue() && BurrowUtil.findHotbarBlock(BlockSkull.class) != -1)
                BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockSkull.class));
        } else if (this.echest.getValue() ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) != -1 : BurrowUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            BurrowUtil.switchToSlot(this.echest.getValue() ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) : BurrowUtil.findHotbarBlock(BlockObsidian.class));
        } else {
            Command.sendMessage("Unable to place burrow block (anvil, ec or oby)");
            this.toggle();
            return;
        }
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.41999998688698, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.7531999805211997, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.00133597911214, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.16610926093821, Burrow.mc.player.posZ, true));
        BurrowUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + (double) this.offset.getValue().intValue(), Burrow.mc.player.posZ, false));
        Burrow.mc.player.connection.sendPacket(new CPacketEntityAction(Burrow.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        Burrow.mc.player.setSneaking(false);
        BurrowUtil.switchToSlot(this.oldSlot);
        this.toggle();
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : Burrow.mc.world.loadedEntityList) {
            if (entity.equals(Burrow.mc.player) || entity instanceof EntityItem || !new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox()))
                continue;
            return true;
        }
        return false;
    }
}

