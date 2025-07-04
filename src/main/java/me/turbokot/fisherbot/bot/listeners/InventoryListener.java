package me.turbokot.fisherbot.bot.listeners;

import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import me.turbokot.fisherbot.bot.Bot;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import java.util.HashMap;
import java.util.Map;

public class InventoryListener extends SessionAdapter {
    private final Bot client;

    public int currentWindowId = 0;
    public Map<Integer, ItemStack> slots = new HashMap<>();

    public InventoryListener(Bot client) {
        this.client = client;
    }

    @Override
    public void packetReceived(PacketReceivedEvent event) {
        if (event.getPacket() instanceof ServerSetSlotPacket packet){
            setSlot(packet.getSlot(), packet.getItem());
            currentWindowId = packet.getWindowId();
        }

        if (event.getPacket() instanceof ServerWindowItemsPacket packet){
            setup(packet.getItems());
        }
    }

    public void setup(ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            slots.put(i, item);
        }
    }

    public void setSlot(int slot, ItemStack item) {
        if (slots.containsKey(slot)) {
            slots.replace(slot, item);
        } else{
            slots.put(slot, item);
        }
    }

    public ItemStack getSlot(int id) {
        return slots.get(id);
    }
}
