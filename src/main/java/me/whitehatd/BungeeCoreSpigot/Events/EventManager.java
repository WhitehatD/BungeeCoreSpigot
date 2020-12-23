package me.whitehatd.BungeeCoreSpigot.Events;

import me.whitehatd.BungeeCoreSpigot.Events.Listener.JoinQuitEvent;
import me.whitehatd.BungeeCoreSpigot.Events.Listener.PreferenceUpdateEvent;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class EventManager {

    public ArrayList<Listener> listeners = new ArrayList<>();

    public EventManager(){
        listeners.add(new JoinQuitEvent());
        listeners.add(new PreferenceUpdateEvent());
    }
}
