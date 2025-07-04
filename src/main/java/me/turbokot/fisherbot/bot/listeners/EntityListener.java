package me.turbokot.fisherbot.bot.listeners;


import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ProjectileData;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import me.turbokot.fisherbot.bot.Bot;
import me.turbokot.fisherbot.utils.ThreadUtils;

import java.util.Map;


public class EntityListener extends SessionAdapter {
    private final Bot client;

    private int fishHookId;
    private long lastCastTime = 0;

    public EntityListener(Bot client) {
        this.client = client;
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        if (event.getPacket() instanceof ServerSpawnObjectPacket object){
            if (object.getType() == ObjectType.FISH_HOOK) {
                Object data = object.getData();
                if (data instanceof ProjectileData projectileData) {
                    if (projectileData.getOwnerId() == client.getEntityId()) {
                        fishHookId = object.getEntityId();
                        lastCastTime = System.currentTimeMillis();
                    }
                }
            }
        }

        if (event.getPacket() instanceof ServerEntityPositionRotationPacket packet){
            if (packet.getEntityId() == fishHookId){
                double movementX = packet.getMovementX();
                double movementY = packet.getMovementY();

                if (System.currentTimeMillis() - lastCastTime < 2000) {
                    return;
                }

                if (movementY < -0.4 && Math.abs(movementX) < 0.1) {
                    client.getSession().send(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
                    client.getSession().send(new ClientPlayerSwingArmPacket(Hand.MAIN_HAND));

                    for (Map.Entry<Integer, ItemStack> entry : client.getInventory().slots.entrySet()) {
                        Integer slot = entry.getKey();
                        ItemStack item = entry.getValue();

                        if (item == null) continue;

                        int itemId = item.getId();
                        int itemAmount = item.getAmount();
                        int adjustedSlot = slot - 36;
                        if (adjustedSlot < 0 || adjustedSlot > 8) continue;

                        if (itemId == 403 || (itemId == 349 || itemId == 350) && itemAmount == 64) {
                            ThreadUtils.sleep(250);
                            client.getSession().send(new ClientPlayerChangeHeldItemPacket(adjustedSlot));
                            ThreadUtils.sleep(150);
                            client.getSession().send(new ClientChatPacket("/ah sell 999999"));
                            ThreadUtils.sleep(150);
                            client.getSession().send(new ClientChatPacket("/ah selling"));
                            ThreadUtils.sleep(250);
                            client.getSession().send(new ClientWindowActionPacket(
                                    client.getInventory().currentWindowId,
                                    2,
                                    adjustedSlot,
                                    client.getInventory().getSlot(slot),
                                    WindowAction.CLICK_ITEM,
                                    ClickItemParam.LEFT_CLICK
                            ));
                        }

                        else if (!(itemId == 349 || itemId == 350 || itemId == 346)) {
                            client.getSession().send(new ClientPlayerChangeHeldItemPacket(adjustedSlot));
                            ThreadUtils.sleep(150);
                            client.getSession().send(new ClientPlayerActionPacket(
                                    PlayerAction.DROP_ITEM_STACK,
                                    client.getPos().translate(),
                                    BlockFace.UP
                            ));
                        }
                    }

                    client.getSession().send(new ClientPlayerChangeHeldItemPacket(0));
                    ThreadUtils.sleep(250);
                    client.getSession().send(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
                    client.getSession().send(new ClientPlayerSwingArmPacket(Hand.MAIN_HAND));
                    client.getInventory().slots.clear();
                }
            }
        }
    }
}