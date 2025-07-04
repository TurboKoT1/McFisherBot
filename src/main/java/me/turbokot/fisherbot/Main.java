package me.turbokot.fisherbot;

import me.turbokot.fisherbot.bot.Bot;
import me.turbokot.fisherbot.config.Config;
import me.turbokot.fisherbot.config.ConfigLoader;
import me.turbokot.fisherbot.utils.Logger;
import me.turbokot.fisherbot.utils.ProxyLoader;

import java.net.Proxy;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Logger.log("Running McFisher..");

        Logger.log("Loading config..");
        ConfigLoader.loadConfig();

        if (Config.use_proxies){
            Logger.log("Loading proxies..");
            ProxyLoader.loadProxies(Config.proxies);
        }

        Logger.log("Launching bots..");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Config.nicknames.size());
        for (Object nickname : Config.nicknames) {

            Runnable botTask;
            if (Config.use_proxies){
                botTask = new Bot(Config.host, Config.port, (String) nickname, ProxyLoader.nextProxy());
            } else{
                botTask = new Bot(Config.host, Config.port, (String) nickname, Proxy.NO_PROXY);
            }

            scheduler.scheduleAtFixedRate(
                    botTask,
                    0,
                    50,
                    TimeUnit.MILLISECONDS
            );
        }

        Logger.log("All is done!");
    }
}
