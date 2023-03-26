package ryo.mrbubblegum.nhack4.world.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import ryo.mrbubblegum.nhack4.world.EventStage;

@Cancelable
public class UpdateWalkingPlayerEvent
        extends EventStage {
    public UpdateWalkingPlayerEvent(int stage) {
        super(stage);
    }
}