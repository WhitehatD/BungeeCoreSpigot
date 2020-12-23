package me.whitehatd.BungeeCoreSpigot.GuiUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Gui {

    private Inventory inventory;
    private Map<Integer, Consumer<Player>> functions = new HashMap<>();
    private Map<Integer, Consumer<Player>> functionSecondary = new HashMap<>();

    public Gui(String name, int rows) {
        this.inventory = Bukkit.createInventory(new GuiHolder(), rows * 9, name);
    }

    public void clearGui() {
        functions.clear();
        functionSecondary.clear();
        this.inventory.clear();
    }

    public abstract void setupGui(Player player);

    public abstract void onClose(Player player);

    public void openGui(Player player) {
        if(player.getOpenInventory() instanceof PlayerInventory)
            player.closeInventory();
        clearGui();
        setupGui(player);
        player.openInventory(inventory);
    }

    public void addButton(int x, int y, ItemStack item, Consumer<Player> function, Consumer<Player> secondary) {
        addButton(x + y * 9, item, function, secondary);
    }

    public void addButton(int x, int y, ItemStack item, Consumer<Player> function) {
        addButton(x + y * 9, item, function, null);
    }

    public void addButton(int slot, ItemStack item, Consumer<Player> function){
        addButton(slot, item, function, null);
    }

    public void addButton(int slot, ItemStack item, Consumer<Player> function, Consumer<Player> secondary) {
        functions.put(slot, function);
        if (secondary != null)
            functionSecondary.put(slot, secondary);
        inventory.setItem(slot, item);
    }

    public void onClick(int slot, Player player, InventoryAction action) {
        if (action == InventoryAction.PICKUP_ALL) {
            if (functions.containsKey(slot)) {
                functions.get(slot).accept(player);
            }
        } else {
            if (functionSecondary.containsKey(slot)) {
                functionSecondary.get(slot).accept(player);
            }
        }
    }

    public class GuiHolder implements InventoryHolder {

        public Gui getGui() {
            return Gui.this;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }

    }

}
