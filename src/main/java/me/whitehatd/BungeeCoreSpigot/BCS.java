package me.whitehatd.BungeeCoreSpigot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.whitehatd.BungeeCoreSpigot.Data.AsyncTask;
import me.whitehatd.BungeeCoreSpigot.Events.EventManager;
import me.whitehatd.BungeeCoreSpigot.GuiUtils.GuiListener;
import me.whitehatd.BungeeCoreSpigot.Redis.RedisListener;
import me.whitehatd.BungeeCoreSpigot.Utilities.*;
import me.whitehatd.BungeeCoreSpigot.Utilities.Config.Config;
import org.bukkit.Bukkit;
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
    public static Jedis jedisSubscriber, jedis;
    public static ArrayList<Thread> asyncThreads = new ArrayList<>();
    public static ProtocolManager protocolManager;


    public void onEnable(){
        instance = this;

        config = new Config("config.yml");
        config.saveDefault();

        JedisPoolConfig pConfig = new JedisPoolConfig();
        pConfig.setTestOnBorrow(true);
        pConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(pConfig, Utils.getConfig().getString("redis-host").split(":")[0],
                Integer.parseInt(Utils.getConfig().getString("redis-host").split(":")[1]));

        jedisSubscriber = jedisPool.getResource();
        jedis = jedisPool.getResource();
        new AsyncTask(() -> jedisSubscriber.subscribe(new RedisListener(), "queue_preferences"));

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
    }

}
