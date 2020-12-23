package me.whitehatd.BungeeCoreSpigot.Utilities;

import me.whitehatd.BungeeCoreSpigot.BCS;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Locale;

public class Utils {

    public static FileConfiguration getConfig(){
        return BCS.config.get();
    }

    public static void execSync(Runnable runnable){
        Bukkit.getServer().getScheduler().runTask(BCS.getInstance(), runnable);
    }

    public static String firstUpper(String str){
        return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1).toLowerCase();
    }

}
