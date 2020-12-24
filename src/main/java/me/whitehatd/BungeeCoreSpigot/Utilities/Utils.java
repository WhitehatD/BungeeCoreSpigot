package me.whitehatd.BungeeCoreSpigot.Utilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import me.whitehatd.BungeeCoreSpigot.BCS;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

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

    public static void registerPacketListeners(){
        /*BCS.protocolManager.addPacketListener(new PacketAdapter(BCS.getInstance(), ListenerPriority.LOWEST,
                PacketType.Play.Server.WORLD_EVENT) {
            @Override
            public void onPacketSending(PacketEvent e) {
                e.getPlayer().sendMessage("WORLD_EVENT");
                e.setCancelled(true);
            }
        });
        BCS.protocolManager.addPacketListener(new PacketAdapter(BCS.getInstance(), ListenerPriority.LOWEST,
                PacketType.Play.Server.ENTITY_EFFECT) {
            @Override
            public void onPacketSending(PacketEvent e) {
                e.getPlayer().sendMessage("ENTITY_EFFECT");
                e.setCancelled(true);
            }
        });
        BCS.protocolManager.addPacketListener(new PacketAdapter(BCS.getInstance(), ListenerPriority.LOWEST,
                PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            @Override
            public void onPacketSending(PacketEvent e) {
                e.getPlayer().sendMessage("NAMED_SOUND_EFFECT");
                e.setCancelled(true);
            }
        });
        BCS.protocolManager.addPacketListener(new PacketAdapter(BCS.getInstance(), ListenerPriority.LOWEST,
                PacketType.Play.Server.REMOVE_ENTITY_EFFECT) {
            @Override
            public void onPacketSending(PacketEvent e) {
                e.getPlayer().sendMessage("REMOVE_ENTITY_EFFECT");
                e.setCancelled(true);
            }
        });
        BCS.protocolManager.addPacketListener(new PacketAdapter(BCS.getInstance(), ListenerPriority.LOWEST,
                PacketType.Play.Server.CUSTOM_SOUND_EFFECT) {
            @Override
            public void onPacketSending(PacketEvent e) {
                e.getPlayer().sendMessage("CUSTOM_SOUND_EFFECT");
                e.setCancelled(true);
            }
        });
        BCS.protocolManager.addPacketListener(new PacketAdapter(BCS.getInstance(), ListenerPriority.HIGHEST,
                PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent e) {
                e.getPlayer().sendMessage("WORLD_PARTICLES");
                e.setCancelled(true);
            }
        });
        BCS.protocolManager.addPacketListener(new PacketAdapter(BCS.getInstance(), ListenerPriority.HIGHEST,
                PacketType.Play.Server.ANIMATION) {
            @Override
            public void onPacketSending(PacketEvent e) {
                e.getPlayer().sendMessage("ANIMATION");
                e.setCancelled(true);
            }
        });*/
        BCS.protocolManager.addPacketListener(new PacketAdapter(BCS.getInstance(), ListenerPriority.HIGHEST,
                Arrays.asList(PacketType.Play.Server.WORLD_PARTICLES,
                        PacketType.Play.Server.ENTITY,
                        PacketType.Play.Server.WORLD_EVENT,
                        PacketType.Play.Server.ENTITY_EFFECT,
                        PacketType.Play.Server.ENTITY_STATUS,
                        PacketType.Play.Server.REMOVE_ENTITY_EFFECT,
                        PacketType.Play.Server.CUSTOM_SOUND_EFFECT,
                        PacketType.Play.Server.NAMED_SOUND_EFFECT,
                        PacketType.Play.Server.ANIMATION,
                        PacketType.Play.Server.ENTITY_SOUND)) {
            @Override
            public void onPacketSending(PacketEvent e) {
                e.getPlayer().sendMessage(e.getPacket().getType().toString());
                e.setCancelled(true);
            }
        });
    }

}
