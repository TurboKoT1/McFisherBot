package me.turbokot.fisherbot.bot.listeners;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusPingPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusPongPacket;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;

public class KeepAlivePingListener extends SessionAdapter {
    @Override
    public void packetReceived(PacketReceivedEvent event) {
        if (((MinecraftProtocol)event.getSession().getPacketProtocol()).getSubProtocol() == SubProtocol.GAME){
            if (event.getPacket() instanceof ClientKeepAlivePacket keepAlivePacket) {
                event.getSession().send(new ServerKeepAlivePacket(keepAlivePacket.getPingId()));
            } else if (event.getPacket() instanceof StatusPingPacket pingPacket) {
                event.getSession().send(new StatusPongPacket(pingPacket.getPingTime()));
            }
        }
    }
}