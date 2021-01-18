package me.whitehatd.BungeeCoreSpigot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.samjakob.spigui.SpiGUI;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import me.whitehatd.BungeeCoreSpigot.Data.AsyncTask;
import me.whitehatd.BungeeCoreSpigot.Events.EventManager;
import me.whitehatd.BungeeCoreSpigot.Data.GuiUtils.GuiListener;
import me.whitehatd.BungeeCoreSpigot.Redis.RedisListener;
import me.whitehatd.BungeeCoreSpigot.Utilities.*;
import me.whitehatd.BungeeCoreSpigot.Utilities.Config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;

public class BCS extends JavaPlugin {

    public static BCS instance = null;
    public static Config config = null;
    public static JedisPool jedisPool;
    public static ArrayList<Thread> asyncThreads = new ArrayList<>();
    public static ProtocolManager protocolManager;
    public static SpiGUI spiGUI;

    public void onEnable(){
        instance = this;
        spiGUI = new SpiGUI(instance);
        spiGUI.setDefaultPaginationButtonBuilder((type, inventory) -> {
            switch (type) {
                case PREV_BUTTON:
                    if (inventory.getCurrentPage() > 0) return new SGButton(new ItemBuilder(Material.ARROW)
                            .name("&a&l\u2190 Previous Page")
                            .lore(
                                    "&aClick to move back to",
                                    "&apage " + inventory.getCurrentPage() + ".")
                            .build()
                    ).withListener(event -> {
                        event.setCancelled(true);
                        inventory.previousPage(event.getWhoClicked());
                    });
                    else return null;

                case CURRENT_BUTTON:
                    return new SGButton(new ItemBuilder(Material.NAME_TAG)
                            .name("&7&lPage " + (inventory.getCurrentPage() + 1) + " of " + inventory.getMaxPage())
                            .lore(
                                    "&7You are currently viewing",
                                    "&7page " + (inventory.getCurrentPage() + 1) + "."
                            ).build()
                    ).withListener(event -> event.setCancelled(true));

                case NEXT_BUTTON:
                    if (inventory.getCurrentPage() < inventory.getMaxPage() - 1) return new SGButton(new ItemBuilder(Material.ARROW)
                            .name("&a&lNext Page \u2192")
                            .lore(
                                    "&aClick to move forward to",
                                    "&apage " + (inventory.getCurrentPage() + 2) + "."
                            ).build()
                    ).withListener(event -> {
                        event.setCancelled(true);
                        inventory.nextPage(event.getWhoClicked());
                    });
                    else return null;

                case UNASSIGNED:
                default:
                    return null;
            }
        });
        config = new Config("config.yml");
        config.saveDefault();

        JedisPoolConfig pConfig = new JedisPoolConfig();
        pConfig.setTestOnBorrow(true);
        pConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(pConfig, Utils.getConfig().getString("redis-host").split(":")[0],
                Integer.parseInt(Utils.getConfig().getString("redis-host").split(":")[1]));
        new AsyncTask(()-> {
            try (Jedis jedis = BCS.jedisPool.getResource()) {
                jedis.subscribe(new RedisListener(), "queue_preferences", "queue_f");
            }
        });

        protocolManager = ProtocolLibrary.getProtocolManager();
        Utils.registerPacketListeners();

        EventManager eventManager = new EventManager();
        for(Listener listener : eventManager.listeners)
            Bukkit.getPluginManager().registerEvents(listener, instance);
        Bukkit.getPluginManager().registerEvents(new GuiListener(), instance);

        Bukkit.getConsoleSender().sendMessage(ChatUtil.c("#21cf2aBungeeCoreSpigot enabled!"));
    }

    public static BCS getInstance(){
        return instance;
    }

    public void onDisable(){
        for(Thread thread : asyncThreads)
            thread.stop();
        jedisPool.destroy();
        for(Player player : Bukkit.getOnlinePlayers())
            player.kickPlayer(ChatUtil.c("&cThe server is restarting, please rejoin!"));
    }


}
