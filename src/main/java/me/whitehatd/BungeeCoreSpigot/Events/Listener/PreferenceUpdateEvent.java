package me.whitehatd.BungeeCoreSpigot.Events.Listener;

import me.whitehatd.BungeeCoreSpigot.BCS;
import me.whitehatd.BungeeCoreSpigot.Data.AsyncTask;
import me.whitehatd.BungeeCoreSpigot.Data.Preferences.SocialPreference;
import me.whitehatd.BungeeCoreSpigot.Events.Event.GameplayPreferenceChangeEvent;
import me.whitehatd.BungeeCoreSpigot.Events.Event.SocialPreferenceChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PreferenceUpdateEvent implements Listener {

    @EventHandler
    public void onGamePrefChangeEvent(GameplayPreferenceChangeEvent e){

    }

    @EventHandler
    public void onSocialPrefChangeEvent(SocialPreferenceChangeEvent e){
        Player player = e.getJedisPlayer().getPlayer();
        switch (e.getPreference()){
            case VISIBILITY:{
                if(e.getToggledTo()){
                    for(Player online : Bukkit.getOnlinePlayers())
                        player.showPlayer(BCS.getInstance(), online);
                }else{
                    for(Player online : Bukkit.getOnlinePlayers())
                        player.hidePlayer(BCS.getInstance(), online);
                }
                break;
            }
            case TOGGLE_CHAT:{
                new AsyncTask(()->BCS.jedisPublisher.publish("execute_cmd", player.getName()+"@"+"party chat"));
                break;
            }
        }
    }
}
