package me.turbokot.fisherbot.config;

import me.turbokot.fisherbot.utils.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class ConfigLoader {
    public static void loadConfig() {
        try {
            Map<String, Object> values = new Yaml().load(new FileInputStream("config.yml"));

            Config.host = (String) values.get("host");
            Config.port = (int) values.get("port");
            Config.use_proxies = (boolean) values.get("use-proxies");
            Config.nicknames = (ArrayList) values.get("nicknames");
            Config.proxies = (ArrayList) values.get("proxies");

        } catch (FileNotFoundException e) {
            Logger.error("Config not found! Creating default config..");
            createDefaultConfig();
            System.exit(1337);
        } catch (Exception e) {
            Logger.error("There was an error during loading the config! Please recheck if it works normally");
            e.printStackTrace();
        }
    }

    private static void createDefaultConfig() {
        String defaultConfig = """
                #-#-#-#-#-#-> McFisher Config <-#-#-#-#-#-#
                
                host: "localhost"
                port: 25567
                
                use-proxies: false
                
                nicknames:
                  - Adolfo
                  - Astolfo
                  - Rudolfo
                proxies: # Format: ip:port (or login:pass@ip:port)
                  - 127.0.0.1
                """;

        try (FileOutputStream fos = new FileOutputStream("config.yml")) {
            fos.write(defaultConfig.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
