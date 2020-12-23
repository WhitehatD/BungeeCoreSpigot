package me.whitehatd.BungeeCoreSpigot.Utilities.Config;

import me.whitehatd.BungeeCoreSpigot.BCS;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Config {

    private FileConfiguration config = null;
    private File configfile = null;
    private String name = "";

    public Config(String name){
        this.name = name;
    }

    public void reload() {
        if (config == null)
            configfile = new File(BCS.getInstance().getDataFolder(), name);
        config = YamlConfiguration.loadConfiguration(configfile);
        Reader defConfigStream = null;
        defConfigStream = new InputStreamReader(Objects.requireNonNull(BCS.getInstance().getResource(name)), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        config.setDefaults(defConfig);
    }

    public FileConfiguration get() {
        if (config == null)
            reload();
        return config;
    }

    public void save() {
        if (config == null || configfile == null)
            return;
        try {
            get().save(configfile);
        } catch (IOException ex) {
        }
    }

    public void saveDefault() {
        if (config == null)
            configfile = new File(BCS.getInstance().getDataFolder(), name);
        if (!configfile.exists())
            BCS.getInstance().saveResource(name, false);
    }
}
