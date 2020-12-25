package me.whitehatd.BungeeCoreSpigot.Data.GuiUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GuiListener implements Listener {

    private static final Set<InventoryAction> allowedActions = new HashSet<InventoryAction>(
            Arrays.asList(InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_HALF));

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof Gui.GuiHolder) {
            Gui.GuiHolder holder = (Gui.GuiHolder) event.getInventory().getHolder();
            if (!allowedActions.contains(event.getAction())
                    || event.getClickedInventory().getType() == InventoryType.PLAYER) {
                event.setCancelled(true);
                return;
            }
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                event.setCancelled(true);
                holder.getGui().onClick(event.getSlot(), (Player)event.getWhoClicked(), event.getAction());
                return;
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof Gui.GuiHolder) {
            Gui.GuiHolder holder = (Gui.GuiHolder) event.getInventory().getHolder();
            holder.getGui().onClose((Player)event.getPlayer());
        }
    }

}