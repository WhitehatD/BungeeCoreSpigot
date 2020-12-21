package me.whitehatd.BungeeCoreSpigot.Inventories;

import me.whitehatd.BungeeCoreSpigot.GuiUtils.Gui;
import me.whitehatd.BungeeCoreSpigot.JedisPlayer.JedisPlayer;
import me.whitehatd.BungeeCoreSpigot.Utilities.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class SocialPreferencesGUI extends Gui {

    private final String prefix = "prefs-inventory.social-prefs";

    public SocialPreferencesGUI(){
        super(ConfigUtil.str("prefs-inventory.social-prefs" + ".name"),
                ConfigUtil.num("prefs-inventory.social-prefs" + ".size")/9);
    }

    @Override
    public void setupGui(Player p) {
        for(String key : Utils.getConfig().getConfigurationSection(prefix + ".items").getKeys(false)){
            int slot = ConfigUtil.num(prefix + ".items." + key + ".slot");
            JedisPlayer jedisPlayer = new JedisPlayer(p);
            Preference pref = Preference.valueOf(key.toUpperCase());
            String tog = jedisPlayer.getPreferences().get(pref)?"enabled":"disabled";
            ItemStack item = new ItemBuilder(Material.valueOf(ConfigUtil.str(prefix + ".toggle-item." + tog + ".type")))
                    .name(ConfigUtil.str(prefix + ".toggle-item." + tog + ".name")
                            .replaceAll("%type%", Utils.firstUpper(String.valueOf(pref).toLowerCase())))
                    .lore(ConfigUtil.arr(prefix + ".toggle-item." + tog + ".lore")).build();
            p.getInventory().addItem(item);
            addButton(slot, item, player -> {
                player.sendMessage("t");
            });
        }

    }

    @Override
    public void onClose(Player player) {

    }
}
