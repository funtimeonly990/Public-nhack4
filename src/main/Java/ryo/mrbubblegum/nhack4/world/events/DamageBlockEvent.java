package ryo.mrbubblegum.nhack4.world.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ryo.mrbubblegum.nhack4.world.EventStage;

public class DamageBlockEvent
        extends EventStage {
    private boolean cancelled;
    private BlockPos blockPos;
    private EnumFacing enumFacing;

    public DamageBlockEvent(BlockPos blockPos, EnumFacing enumFacing) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public EnumFacing getEnumFacing() {
        return this.enumFacing;
    }

    public void setEnumFacing(EnumFacing enumFacing) {
        this.enumFacing = enumFacing;
    }
}

