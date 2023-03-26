package ryo.mrbubblegum.nhack4.world.events;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import ryo.mrbubblegum.nhack4.world.EventStage;

public class BlockCollisionBoundingBoxEvent
        extends EventStage {
    private final BlockPos pos;
    private AxisAlignedBB _boundingBox;

    public BlockCollisionBoundingBoxEvent(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public AxisAlignedBB getBoundingBox() {
        return _boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this._boundingBox = boundingBox;
    }
}