package ryo.mrbubblegum.nhack4.world.events;

import net.minecraft.util.math.BlockPos;
import ryo.mrbubblegum.nhack4.world.EventStage;

public class BlockBreakingEvent
        extends EventStage {
    public BlockPos pos;
    public int breakingID;
    public int breakStage;

    public BlockBreakingEvent(BlockPos pos, int breakingID, int breakStage) {
        this.pos = pos;
        this.breakingID = breakingID;
        this.breakStage = breakStage;
    }
}

