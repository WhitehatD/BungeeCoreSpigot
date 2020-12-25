package me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class JedisPlayerManager {

    public static HashMap<Player, JedisPlayer> jedisPlayers = new HashMap<>();

    public static JedisPlayer getByPlayer(Player player){
        return jedisPlayers.get(player);
    }

    public static void registerJedisPlayer(JedisPlayer jedisPlayer){
        jedisPlayers.put(jedisPlayer.getPlayer(), jedisPlayer);
    }

    public static void unregisterJedisPlayer(JedisPlayer jedisPlayer){
        jedisPlayers.remove(jedisPlayer.getPlayer());
    }
}
