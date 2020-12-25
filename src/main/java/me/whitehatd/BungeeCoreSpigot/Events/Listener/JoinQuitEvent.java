package me.whitehatd.BungeeCoreSpigot.Events.Listener;

import me.whitehatd.BungeeCoreSpigot.BCS;
import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer.JedisPlayer;
import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer.JedisPlayerManager;
import me.whitehatd.BungeeCoreSpigot.Data.Preferences.SocialPreference;
import me.whitehatd.BungeeCoreSpigot.Utilities.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        JedisPlayer jedisPlayer = new JedisPlayer(e.getPlayer());
        if(!jedisPlayer.socialStatus(SocialPreference.VISIBILITY)){
            for(Player online : Bukkit.getOnlinePlayers())
                e.getPlayer().hidePlayer(BCS.getInstance(), online);
            ChatUtil.cs(e.getPlayer(), "messages.hidden-players-join");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        JedisPlayerManager.unregisterJedisPlayer(
                JedisPlayerManager.getByPlayer(e.getPlayer()));
    }
}
