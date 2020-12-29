package me.whitehatd.BungeeCoreSpigot.Utilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import me.whitehatd.BungeeCoreSpigot.BCS;
import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer.JedisPlayer;
import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer.JedisPlayerManager;
import me.whitehatd.BungeeCoreSpigot.Data.Preferences.GameplayPreference;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

public class Utils {

    public static FileConfiguration getConfig(){
        return BCS.config.get();
    }

    public static void execSync(Runnable runnable){
        Bukkit.getServer().getScheduler().runTask(BCS.getInstance(), runnable);
    }

    public static String firstUpper(String str){
        final String[] f = {""};
        Arrays.stream(str.split("_")).forEach(s -> {
            f[0] = f[0] + String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1).toLowerCase() + " ";
        });
        return f[0].substring(0, f[0].length()-1);
    }

    public static void registerPacketListeners(){
        BCS.protocolManager.addPacketListener(new PacketAdapter(BCS.getInstance(), ListenerPriority.HIGHEST,
                Arrays.asList(
                        PacketType.Play.Server.WORLD_PARTICLES,
                        PacketType.Play.Server.WORLD_EVENT,
                        PacketType.Play.Server.ANIMATION)) {
            @Override
            public void onPacketSending(PacketEvent e) {
                JedisPlayer jedisPlayer = JedisPlayerManager.getByPlayer(e.getPlayer());
                if(!jedisPlayer.gameplayStatus(GameplayPreference.PARTICLES))
                    e.setCancelled(true);
            }
        });
        BCS.protocolManager.addPacketListener(new PacketAdapter(BCS.getInstance(), ListenerPriority.HIGHEST,
                Arrays.asList(
                        PacketType.Play.Server.CUSTOM_SOUND_EFFECT,
                        PacketType.Play.Server.NAMED_SOUND_EFFECT,
                        PacketType.Play.Server.ENTITY_SOUND)) {
            @Override
            public void onPacketSending(PacketEvent e) {
                JedisPlayer jedisPlayer = JedisPlayerManager.getByPlayer(e.getPlayer());
                if(!jedisPlayer.gameplayStatus(GameplayPreference.SOUND))
                    e.setCancelled(true);

            }
        });
    }

}
