package me.whitehatd.BungeeCoreSpigot.Redis;

import me.whitehatd.BungeeCoreSpigot.Utilities.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

public class RedisListener extends JedisPubSub {

    public void onMessage(String channel, String message) {
        if (channel.equals("queue_preferences")) {
            Player player = Bukkit.getPlayer(UUID.fromString(message));
            player.sendMessage(ChatUtil.c("&dSALUT" + player.getName()));

        }
    }
}
