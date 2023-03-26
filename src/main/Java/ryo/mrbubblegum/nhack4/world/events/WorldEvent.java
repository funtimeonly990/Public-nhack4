package ryo.mrbubblegum.nhack4.world.events;

import net.minecraft.client.multiplayer.WorldClient;
import ryo.mrbubblegum.nhack4.world.EventStage;

public class WorldEvent
        extends EventStage {
    private final WorldClient world;

    public WorldEvent(WorldClient worldIn) {
        this.world = worldIn;
    }

    public WorldClient getWorld() {
        return this.world;
    }
}