package me.whitehatd.BungeeCoreSpigot.Redis;

import me.whitehatd.BungeeCoreSpigot.Inventories.MainPreferencesGUI;
import me.whitehatd.BungeeCoreSpigot.Utilities.Config.ConfigUtil;
import me.whitehatd.BungeeCoreSpigot.Utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;
import java.util.concurrent.*;

public class RedisListener extends JedisPubSub {

    public void onMessage(String channel, String message) {
        if (channel.equals("queue_preferences")) {
            if(!message.split("@")[1].equalsIgnoreCase(ConfigUtil.str("servername")))return;
            Future<Player> future;
            ExecutorService service = Executors.newSingleThreadExecutor();
            Callable<Player> task = () -> Bukkit.getPlayer(UUID.fromString(message.split("@")[0]));
            future = service.submit(task);
            Utils.execSync(()-> {
                try {
                    new MainPreferencesGUI().openGui(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
