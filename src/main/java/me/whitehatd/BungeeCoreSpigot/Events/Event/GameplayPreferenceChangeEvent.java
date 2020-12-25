package me.whitehatd.BungeeCoreSpigot.Events.Event;

import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer.JedisPlayer;
import me.whitehatd.BungeeCoreSpigot.Data.Preferences.GameplayPreference;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class GameplayPreferenceChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private JedisPlayer jedisPlayer;
    private GameplayPreference preference;
    private boolean status;
    private boolean isCancelled;

    public GameplayPreferenceChangeEvent(JedisPlayer jedisPlayer, GameplayPreference preference, boolean status){
        this.jedisPlayer = jedisPlayer;
        this.preference = preference;
        this.status = status;
        this.isCancelled = false;
    }

    public JedisPlayer getJedisPlayer(){
        return jedisPlayer;
    }

    public GameplayPreference getPreference(){
        return preference;
    }

    public boolean getToggledTo(){
        return status;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
