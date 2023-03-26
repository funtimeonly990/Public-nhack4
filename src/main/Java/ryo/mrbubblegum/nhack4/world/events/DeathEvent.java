package ryo.mrbubblegum.nhack4.world.events;

import net.minecraft.entity.player.EntityPlayer;
import ryo.mrbubblegum.nhack4.world.EventStage;

public class DeathEvent
        extends EventStage {
    public EntityPlayer player;

    public DeathEvent(EntityPlayer player) {
        this.player = player;
    }
}

