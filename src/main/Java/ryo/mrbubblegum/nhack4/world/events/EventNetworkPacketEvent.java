package ryo.mrbubblegum.nhack4.world.events;

import net.minecraft.network.Packet;
import ryo.mrbubblegum.nhack4.world.EventStage;

public class EventNetworkPacketEvent extends EventStage {
    public Packet m_Packet;

    public EventNetworkPacketEvent(Packet p_Packet) {
        super();
        m_Packet = p_Packet;
    }

    public Packet GetPacket() {
        return m_Packet;
    }

    public Packet getPacket() {
        return m_Packet;
    }
}