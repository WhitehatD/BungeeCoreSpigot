package me.whitehatd.BungeeCoreSpigot.JedisPlayer;

import me.whitehatd.BungeeCoreSpigot.BCS;
import me.whitehatd.BungeeCoreSpigot.Utilities.Preference;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class JedisPlayer {

    private Future<Map<Preference, Boolean>> futurePreference;
    private Player player;

    public JedisPlayer(Player player){
        this.player = player;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Map<Preference, Boolean>> task = () -> {
            Map<Preference, Boolean> preferences =  new HashMap<>();
            Map<String, String> rawMap;
            rawMap = BCS.jedis.hgetAll(player.getUniqueId().toString());
            if(rawMap.isEmpty()){
                Map<String, String> toAdd = new HashMap<>();
                for(Preference preference : Preference.values()) {
                    toAdd.put(String.valueOf(preference), "false");
                    preferences.put(preference, false);
                }
                BCS.jedis.hmset(player.getUniqueId().toString(), toAdd);
            } else {
                rawMap.keySet().forEach(key -> preferences.put(Preference.valueOf(key),
                        Boolean.valueOf(rawMap.get(key))));
            }
            return preferences;
        };
        futurePreference = executor.submit(task);
    }

    public Player getPlayer(){
        return player;
    }

    public Map<Preference, Boolean> getPreferences(){
        try {
            return futurePreference.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
