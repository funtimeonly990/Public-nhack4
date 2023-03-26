package ryo.mrbubblegum.nhack4.world.events;

import net.minecraft.entity.player.EntityPlayer;
import ryo.mrbubblegum.nhack4.world.EventStage;

public class TotemPopEvent
        extends EventStage {
    private final EntityPlayer entity;

    public TotemPopEvent(EntityPlayer entity) {
        this.entity = entity;
    }

    public EntityPlayer getEntity() {
        return this.entity;
    }
}
