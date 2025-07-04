package me.turbokot.fisherbot.bot;

import me.turbokot.fisherbot.bot.data.Vector3D;
import me.turbokot.fisherbot.bot.listeners.*;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;

import lombok.Getter;
import lombok.Setter;

import java.net.Proxy;

@Getter
@Setter
public class Bot implements Runnable {
    private final String host;
    private final int port;
    private final MinecraftProtocol account;

    private boolean connected;
    private int entityId;

    Living living;
    EntityListener entity;
    InventoryListener inventory;

    private Session session;
    private Vector3D pos;
    private float health = 20;

    public Bot(String host, int port, String username, Proxy proxy) {
        this.host = host;
        this.port = port;
        this.account = new MinecraftProtocol(username);

        this.build(proxy);
    }

    public void build(Proxy proxy) {
        Client client = new Client(host, port, account, new TcpSessionFactory(proxy));

        living = new Living(this);
        entity = new EntityListener(this);
        inventory = new InventoryListener(this);

        session = client.getSession();
        session.addListener(new KeepAlivePingListener());
        session.addListener(new SessionListener(this));
        session.addListener(living);
        session.addListener(entity);
        session.addListener(inventory);
        session.setConnectTimeout(3);

        session.connect();
    }

    @Override
    public void run() {
        if (isConnected()) tick();
    }

    public void tick() {
        living.tick();
    }
}
