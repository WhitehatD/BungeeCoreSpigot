package me.whitehatd.BungeeCoreSpigot.Utilities;

import me.whitehatd.BungeeCoreSpigot.BCS;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class Utils {

    public static FileConfiguration getConfig(){
        return BCS.config.get();
    }

    public static void execAsync(Runnable runnable){
        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(BCS.getInstance(), runnable, 5L);
    }

}
