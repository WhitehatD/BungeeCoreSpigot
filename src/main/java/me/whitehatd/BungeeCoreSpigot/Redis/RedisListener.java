package me.whitehatd.BungeeCoreSpigot.Redis;

import me.whitehatd.BungeeCoreSpigot.Inventories.MainPreferencesGUI;
import me.whitehatd.BungeeCoreSpigot.Utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;
import java.util.UUID;

public class RedisListener extends JedisPubSub {

    public void onMessage(String channel, String message) {
        if (channel.equals("queue_preferences")) {
            Utils.execSync(()-> {
                Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(message)));
                new MainPreferencesGUI().openGui(player);
            });
        }
    }
}
