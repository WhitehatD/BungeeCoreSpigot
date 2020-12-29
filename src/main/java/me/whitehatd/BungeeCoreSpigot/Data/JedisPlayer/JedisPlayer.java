package me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer;

import me.whitehatd.BungeeCoreSpigot.BCS;
import me.whitehatd.BungeeCoreSpigot.Data.AsyncTask;
import me.whitehatd.BungeeCoreSpigot.Data.Preferences.GameplayPreference;
import me.whitehatd.BungeeCoreSpigot.Data.Preferences.SocialPreference;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class JedisPlayer {

    private Future<Map<SocialPreference, Boolean>> futureSocialPreference;
    private Future<Map<GameplayPreference, Boolean>> futureGameplayPreference;
    private Player player;

    public JedisPlayer(Player player){
        this.player = player;
        refresh();
        JedisPlayerManager.registerJedisPlayer(this);
    }

    public Player getPlayer(){
        return player;
    }

    public Map<SocialPreference, Boolean> getSocialPreferences(){
        try {
            return futureSocialPreference.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<GameplayPreference, Boolean> getGameplayPreferences(){
        try {
            return futureGameplayPreference.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean socialStatus(SocialPreference pref){
        return getSocialPreferences().get(pref);
    }

    public boolean gameplayStatus(GameplayPreference pref){
        return getGameplayPreferences().get(pref);
    }

    public void setSocialPreference(SocialPreference pref, boolean toggle){
        new AsyncTask(()->{
            Map<SocialPreference, Boolean> preferences = getSocialPreferences();
            Map<String, String> rawMap = new HashMap<>();
            preferences.put(pref, toggle);
            for(SocialPreference preference : preferences.keySet())
                rawMap.put(String.valueOf(preference), String.valueOf(preferences.get(preference)));
            try(Jedis jedis = BCS.jedisPool.getResource()) {
                jedis.hmset(player.getUniqueId().toString() + "@social", rawMap);
            }
        });
    }

    public void setGameplayPreference(GameplayPreference pref, boolean toggle){
        new AsyncTask(()->{
            Map<GameplayPreference, Boolean> preferences = getGameplayPreferences();
            Map<String, String> rawMap = new HashMap<>();
            preferences.put(pref, toggle);
            for(GameplayPreference preference : preferences.keySet())
                rawMap.put(String.valueOf(preference), String.valueOf(preferences.get(preference)));
            try(Jedis jedis = BCS.jedisPool.getResource()) {
                jedis.hmset(player.getUniqueId().toString() + "@gameplay", rawMap);
            }
        });
    }

    public void refresh(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Map<SocialPreference, Boolean>> social = () -> {
            Map<SocialPreference, Boolean> preferences =  new HashMap<>();
            Map<String, String> rawMap;
            try(Jedis jedis = BCS.jedisPool.getResource()) {
                rawMap = jedis.hgetAll(player.getUniqueId().toString() + "@social");
            }
            if(rawMap.isEmpty()){
                Map<String, String> toAdd = new HashMap<>();
                for(SocialPreference preference : SocialPreference.values()) {
                    toAdd.put(String.valueOf(preference), "false");
                    preferences.put(preference, false);
                }
                try(Jedis jedis = BCS.jedisPool.getResource()) {
                    jedis.hmset(player.getUniqueId().toString() + "@social", toAdd);
                }
            } else {
                rawMap.keySet().forEach(key -> preferences.put(SocialPreference.valueOf(key),
                        Boolean.valueOf(rawMap.get(key))));
            }
            return preferences;
        };
        futureSocialPreference = executor.submit(social);
        Callable<Map<GameplayPreference, Boolean>> gameplay = () -> {
            Map<GameplayPreference, Boolean> preferences =  new HashMap<>();
            Map<String, String> rawMap;
            try(Jedis jedis = BCS.jedisPool.getResource()) {
                rawMap = jedis.hgetAll(player.getUniqueId().toString() + "@gameplay");
            }
            if(rawMap.isEmpty()){
                Map<String, String> toAdd = new HashMap<>();
                for(GameplayPreference preference : GameplayPreference.values()) {
                    toAdd.put(String.valueOf(preference), "false");
                    preferences.put(preference, false);
                }
                try(Jedis jedis = BCS.jedisPool.getResource()) {
                    jedis.hmset(player.getUniqueId().toString() + "@gameplay", toAdd);
                }
            } else {
                rawMap.keySet().forEach(key -> preferences.put(GameplayPreference.valueOf(key),
                        Boolean.valueOf(rawMap.get(key))));
            }
            return preferences;
        };
        futureGameplayPreference = executor.submit(gameplay);
    }
}
