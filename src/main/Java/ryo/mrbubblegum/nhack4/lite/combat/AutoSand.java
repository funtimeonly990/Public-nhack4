package ryo.mrbubblegum.nhack4.lite.combat;

import net.minecraft.block.BlockSand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ryo.mrbubblegum.nhack4.impl.util.BlockUtil;
import ryo.mrbubblegum.nhack4.impl.util.InventoryUtil;
import ryo.mrbubblegum.nhack4.lite.Module;

import java.util.Comparator;


public class AutoSand extends Module {
    EntityPlayer entityPlayer;

    public AutoSand() {
        super("AutoSand", "Automatically places sand on players.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        this.placeSand();
        this.disable();
    }

    public void placeSand() {
        this.entityPlayer = mc.world.playerEntities.stream().filter(p -> p.getEntityId() != AutoSand.mc.player.getEntityId()).min(Comparator.comparing(p -> Float.valueOf(p.getDistance(AutoSand.mc.player)))).orElse(null);
        if (entityPlayer != null) {
            BlockPos sandPlacePos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY + 2), Math.floor(entityPlayer.posZ));
            BlockPos playerPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY), Math.floor(entityPlayer.posZ));

            int sand = InventoryUtil.findHotbarBlock(BlockSand.class);
            int previousSlot = mc.player.inventory.currentItem;

            int canPlace = BlockUtil.isPositionPlaceable(sandPlacePos, false, true);

            if (canPlace != -1 && entityPlayer != null && sand != -1 && sandPlacePos != null && mc.world.getBlockState(playerPos).getBlock() == Blocks.AIR) {
                mc.player.inventory.currentItem = sand;
                mc.playerController.updateController();
                BlockUtil.placeBlock(sandPlacePos, EnumHand.MAIN_HAND, false, true, false);
                mc.player.inventory.currentItem = previousSlot;
            } else {
                this.disable();
            }
        }
    }
}