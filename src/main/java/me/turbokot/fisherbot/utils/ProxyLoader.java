package me.turbokot.fisherbot.utils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyLoader {
    public static List<Proxy> proxies = new ArrayList<>();
    private static final AtomicInteger index = new AtomicInteger(0);

    public static void loadProxy(String proxyString) {
        String[] hostPort = proxyString.split(":");
        String host = hostPort[0];
        int port = Integer.parseInt(hostPort[1]);

        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));

        proxies.add(proxy);
    }

    public static void loadProxies(ArrayList proxies) {
        for (Object proxy : proxies){
            loadProxy((String) proxy);
        }
    }

    public static Proxy nextProxy(){
        int currentIndex = index.getAndIncrement();
        if (currentIndex >= proxies.size()) {
            index.set(0);
            currentIndex = 0;
        }

        return proxies.get(currentIndex);
    }
}
