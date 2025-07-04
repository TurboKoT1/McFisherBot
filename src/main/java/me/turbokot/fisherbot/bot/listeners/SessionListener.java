package me.turbokot.fisherbot.bot.listeners;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.SkinPart;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import me.turbokot.fisherbot.bot.Bot;
import me.turbokot.fisherbot.bot.data.Vector3D;

import java.util.ArrayList;

public class SessionListener extends SessionAdapter {
    private final Bot client;

    public SessionListener(Bot client) {
        this.client = client;
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        if (event.getPacket() instanceof ServerJoinGamePacket packet) {
            client.getSession().send(new ClientSettingsPacket(
                    "ru",
                    16,
                    ChatVisibility.FULL,
                    true,
                    new ArrayList<SkinPart>() {
                        private static final long serialVersionUID = 1562002041284663871L;

                        {
                            add(SkinPart.CAPE);
                            add(SkinPart.HAT);
                            add(SkinPart.JACKET);
                            add(SkinPart.LEFT_PANTS_LEG);
                            add(SkinPart.LEFT_SLEEVE);
                            add(SkinPart.RIGHT_PANTS_LEG);
                            add(SkinPart.RIGHT_SLEEVE);
                        }
                    }.toArray(new SkinPart[0]),
                    Hand.MAIN_HAND
            ));

            client.setEntityId(packet.getEntityId());
            client.setConnected(true);
            client.getSession().send(new ClientPluginMessagePacket("minecraft:brand", new byte[]{7, 118, 97, 110, 105, 108, 108, 97}));
        } else if (event.getPacket() instanceof ServerSpawnPositionPacket packet) {
            Position pos = packet.getPosition();
            client.setPos(new Vector3D(pos.getX(), pos.getY(), pos.getZ()));

        } else if (event.getPacket() instanceof ServerPlayerPositionRotationPacket pos) {
            client.setPos(new Vector3D(pos.getX(), pos.getY(), pos.getZ()));

        } else if (event.getPacket() instanceof ServerChatPacket) {
            if (((ServerChatPacket) event.getPacket()).getMessage().getFullText().toLowerCase().contains("startfish")) {
                client.getSession().send(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
                client.getSession().send(new ClientPlayerSwingArmPacket(Hand.MAIN_HAND));
            }
        }
    }

    @Override
    public void disconnected(DisconnectedEvent event) {
        System.out.println(event.getReason());
    }
}