package ryo.mrbubblegum.nhack4.lite.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ryo.mrbubblegum.nhack4.impl.util.BlockUtil;
import ryo.mrbubblegum.nhack4.impl.util.EntityUtil;
import ryo.mrbubblegum.nhack4.impl.util.InventoryUtil;
import ryo.mrbubblegum.nhack4.impl.util.utils.EntityUtils;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.loader.Loader;
import ryo.mrbubblegum.nhack4.system.command.Command;
import ryo.mrbubblegum.nhack4.system.setting.Setting;

public class PistonPush
        extends Module {
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> disable = this.register(new Setting<Boolean>("Disable", true));
    private final Setting<Boolean> noGhost = this.register(new Setting<Boolean>("Packet", false));
    private final Setting<Integer> deay = this.register(new Setting<Integer>("Deay", 30, 0, 100));
    private final Setting<Double> range = this.register(new Setting<Double>("Range", 4.0, 0.0, 10.0));
    private int tick;
    private boolean gs = true;

    public PistonPush() {
        super("Elevator", "worst than private one", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (this.tick != 90 && this.tick++ >= this.deay.getValue()) {
            this.tick = 0;
            this.gs = true;
        }
        if (PistonPush.mc.player == null || PistonPush.mc.player.isDead) {
            return;
        }
        if (PistonPush.nullCheck()) {
            return;
        }
        if (this.disable.getValue().booleanValue()) {
            this.disable();
        }
        if (InventoryUtil.findHotbarBlock(Blocks.PISTON) == -1) {
            if (this.disable.getValue().booleanValue()) {
                Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No PISTON...");
            }
            return;
        }
        if (InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK) == -1) {
            if (this.disable.getValue().booleanValue()) {
                Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No REDSTONE BLOCK...");
            }
            return;
        }
        if (InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN) == -1) {
            if (this.disable.getValue().booleanValue()) {
                Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No PISTON...");
            }
            return;
        }
        EntityPlayer target = this.getTarget(this.range.getValue(), true);
        if (this.gs) {
            this.gs = false;
            if (target != null && PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY, target.posZ)).getBlock() != Blocks.AIR) {
                if (PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY + 2.0, target.posZ)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX + 1.0, target.posY + 1.0, target.posZ)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX - 1.0, target.posY + 1.0, target.posZ)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX + 2.0, target.posY + 1.0, target.posZ)).getBlock() == Blocks.AIR) {
                    int imp;
                    if (PistonPush.mc.world.getBlockState(new BlockPos(target.posX + 2.0, target.posY, target.posZ)).getBlock() == Blocks.AIR) {
                        imp = PistonPush.mc.player.inventory.currentItem;
                        PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                        BlockUtil.placeBlock(new BlockPos(target.posX + 2.0, target.posY, target.posZ), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                        PistonPush.mc.player.inventory.currentItem = imp;
                    }
                    imp = PistonPush.mc.player.inventory.currentItem;
                    PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
                    BlockUtil.placeBlock(new BlockPos(target.posX + 2.0, target.posY + 1.0, target.posZ), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                    PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.PISTON);
                    BlockUtil.placeBlock(new BlockPos(target.posX + 1.0, target.posY + 1.0, target.posZ), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                    PistonPush.mc.player.inventory.currentItem = imp;
                    return;
                }
                if (PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY + 2.0, target.posZ)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY + 1.0, target.posZ + 1.0)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY + 1.0, target.posZ - 1.0)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY + 1.0, target.posZ + 2.0)).getBlock() == Blocks.AIR) {
                    int imp;
                    if (PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY, target.posZ + 2.0)).getBlock() == Blocks.AIR) {
                        imp = PistonPush.mc.player.inventory.currentItem;
                        PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                        BlockUtil.placeBlock(new BlockPos(target.posX, target.posY, target.posZ + 2.0), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                        PistonPush.mc.player.inventory.currentItem = imp;
                    }
                    imp = PistonPush.mc.player.inventory.currentItem;
                    PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
                    BlockUtil.placeBlock(new BlockPos(target.posX, target.posY + 1.0, target.posZ + 2.0), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                    PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.PISTON);
                    BlockUtil.placeBlock(new BlockPos(target.posX, target.posY + 1.0, target.posZ + 1.0), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                    PistonPush.mc.player.inventory.currentItem = imp;
                    return;
                }
                if (PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY + 2.0, target.posZ)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY + 1.0, target.posZ - 1.0)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY + 1.0, target.posZ + 1.0)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY + 1.0, target.posZ - 2.0)).getBlock() == Blocks.AIR) {
                    int imp;
                    if (PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY, target.posZ - 2.0)).getBlock() == Blocks.AIR) {
                        imp = PistonPush.mc.player.inventory.currentItem;
                        PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                        BlockUtil.placeBlock(new BlockPos(target.posX, target.posY, target.posZ - 2.0), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                        PistonPush.mc.player.inventory.currentItem = imp;
                    }
                    imp = PistonPush.mc.player.inventory.currentItem;
                    PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
                    BlockUtil.placeBlock(new BlockPos(target.posX, target.posY + 1.0, target.posZ - 2.0), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                    PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.PISTON);
                    BlockUtil.placeBlock(new BlockPos(target.posX, target.posY + 1.0, target.posZ - 1.0), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                    PistonPush.mc.player.inventory.currentItem = imp;
                    return;
                }
                if (PistonPush.mc.world.getBlockState(new BlockPos(target.posX, target.posY + 2.0, target.posZ)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX - 1.0, target.posY + 1.0, target.posZ)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX + 1.0, target.posY + 1.0, target.posZ)).getBlock() == Blocks.AIR && PistonPush.mc.world.getBlockState(new BlockPos(target.posX - 2.0, target.posY + 1.0, target.posZ)).getBlock() == Blocks.AIR) {
                    int imp;
                    if (PistonPush.mc.world.getBlockState(new BlockPos(target.posX - 2.0, target.posY, target.posZ)).getBlock() == Blocks.AIR) {
                        imp = PistonPush.mc.player.inventory.currentItem;
                        PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                        BlockUtil.placeBlock(new BlockPos(target.posX - 2.0, target.posY, target.posZ), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                        PistonPush.mc.player.inventory.currentItem = imp;
                    }
                    imp = PistonPush.mc.player.inventory.currentItem;
                    PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
                    BlockUtil.placeBlock(new BlockPos(target.posX - 2.0, target.posY + 1.0, target.posZ), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                    PistonPush.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.PISTON);
                    BlockUtil.placeBlock(new BlockPos(target.posX - 1.0, target.posY + 1.0, target.posZ), EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), true);
                    PistonPush.mc.player.inventory.currentItem = imp;
                }
            }
        }
    }

    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range) || trapped && EntityUtils.isTrapped(player, false, false, false, false, false) || Loader.speedManager.getPlayerSpeed(player) > 10.0)
                continue;
            if (target == null) {
                target = player;
                distance = AutoTrap.mc.player.getDistanceSq(player);
                continue;
            }
            if (!(AutoTrap.mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = AutoTrap.mc.player.getDistanceSq(player);
        }
        return target;
    }
}