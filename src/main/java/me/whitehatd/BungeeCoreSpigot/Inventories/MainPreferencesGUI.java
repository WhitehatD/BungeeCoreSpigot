package me.whitehatd.BungeeCoreSpigot.Inventories;

import me.whitehatd.BungeeCoreSpigot.GuiUtils.Gui;
import me.whitehatd.BungeeCoreSpigot.Data.ItemBuilder;
import me.whitehatd.BungeeCoreSpigot.Utilities.Config.ConfigUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainPreferencesGUI extends Gui {

    public MainPreferencesGUI(){
        super(ConfigUtil.str("prefs-inventory.name"), ConfigUtil.num("prefs-inventory.size")/9);
    }


    @Override
    public void setupGui(Player p) {
        String socialPrefix = "prefs-inventory.social-prefs";
        String gamePrefix = "prefs-inventory.gameplay-prefs";
        ItemStack socialPrefs = new ItemBuilder(Material.valueOf(ConfigUtil.str(socialPrefix + ".item.type")))
                .name(ConfigUtil.str(socialPrefix + ".item.name"))
                .lore(ConfigUtil.arr(socialPrefix + ".item.lore"))
                .build();
        addButton(ConfigUtil.num(socialPrefix + ".item.slot"), socialPrefs,
               player -> new SocialPreferencesGUI().openGui(player));

        ItemStack gamePrefs = new ItemBuilder(Material.valueOf(ConfigUtil.str(gamePrefix + ".item.type")))
                .name(ConfigUtil.str(gamePrefix + ".item.name"))
                .lore(ConfigUtil.arr(gamePrefix + ".item.lore"))
                .build();
        addButton(ConfigUtil.num(gamePrefix + ".item.slot"), gamePrefs,
                player -> new GameplayPreferencesGUI().openGui(player));
    }

    @Override
    public void onClose(Player player) {

    }
}
