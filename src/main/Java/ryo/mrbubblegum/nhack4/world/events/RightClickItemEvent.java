package ryo.mrbubblegum.nhack4.world.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ryo.mrbubblegum.nhack4.world.EventStage;

public class RightClickItemEvent
        extends EventStage {
    private final EntityPlayer player;
    private final World worldIn;
    private final EnumHand hand;

    public RightClickItemEvent(EntityPlayer player, World worldIn, EnumHand hand) {
        this.player = player;
        this.worldIn = worldIn;
        this.hand = hand;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public World getWorldIn() {
        return this.worldIn;
    }

    public EnumHand getHand() {
        return this.hand;
    }
}

