package me.whitehatd.BungeeCoreSpigot.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Inventories {

    private Player player;

    public Inventories(Player player){
        this.player = player;
    }

    public Inventory getMainPreferencesInventory(){
        Inventory inv = Bukkit.createInventory(null, );
        return inv;
    }
}
