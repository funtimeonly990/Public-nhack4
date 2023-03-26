package ryo.mrbubblegum.nhack4.world.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import ryo.mrbubblegum.nhack4.world.EventStage;

@Cancelable
public class ChatEvent
        extends EventStage {
    private final String msg;

    public ChatEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}

