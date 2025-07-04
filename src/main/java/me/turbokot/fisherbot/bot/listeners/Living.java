package me.turbokot.fisherbot.bot.listeners;

import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;

import me.turbokot.fisherbot.bot.Bot;

public class Living extends SessionAdapter {
    private final Bot client;

    private int sleepticks = 50;

    public Living(Bot client) {
        this.client = client;
    }

    @Override
    public void packetReceived(PacketReceivedEvent receiveEvent) {
        if (receiveEvent.getPacket() instanceof ServerPlayerHealthPacket) {
            client.setHealth(((ServerPlayerHealthPacket) receiveEvent.getPacket()).getHealth());
        }
    }

    public void tick() {
        if (sleepticks > 0) {
            sleepticks--;
            return;
        }

        if (client.getHealth() <= 0) {
            client.getSession().send(new ClientRequestPacket(ClientRequest.RESPAWN));
        }
    }
}