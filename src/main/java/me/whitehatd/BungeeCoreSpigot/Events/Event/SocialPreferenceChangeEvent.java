package me.whitehatd.BungeeCoreSpigot.Events.Event;

import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer.JedisPlayer;
import me.whitehatd.BungeeCoreSpigot.Data.Preferences.SocialPreference;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class SocialPreferenceChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private JedisPlayer jedisPlayer;
    private SocialPreference preference;
    private boolean status;
    private boolean isCancelled;

    public SocialPreferenceChangeEvent(JedisPlayer jedisPlayer, SocialPreference preference, boolean status){
        this.jedisPlayer = jedisPlayer;
        this.preference = preference;
        this.status = status;
        this.isCancelled = false;
    }

    public JedisPlayer getJedisPlayer(){
        return jedisPlayer;
    }

    public SocialPreference getPreference(){
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
