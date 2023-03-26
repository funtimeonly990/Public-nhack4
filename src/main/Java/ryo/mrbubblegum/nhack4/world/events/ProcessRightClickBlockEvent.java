package ryo.mrbubblegum.nhack4.world.events;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import ryo.mrbubblegum.nhack4.world.EventStage;

@Cancelable
public class ProcessRightClickBlockEvent
        extends EventStage {
    public BlockPos pos;
    public EnumHand hand;
    public ItemStack stack;

    public ProcessRightClickBlockEvent(BlockPos pos, EnumHand hand, ItemStack stack) {
        this.pos = pos;
        this.hand = hand;
        this.stack = stack;
    }
}

