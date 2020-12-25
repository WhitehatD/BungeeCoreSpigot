package me.whitehatd.BungeeCoreSpigot.Inventories;

import me.whitehatd.BungeeCoreSpigot.Data.ItemBuilder;
import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer.JedisPlayerManager;
import me.whitehatd.BungeeCoreSpigot.Data.Preferences.SocialPreference;
import me.whitehatd.BungeeCoreSpigot.Events.Event.SocialPreferenceChangeEvent;
import me.whitehatd.BungeeCoreSpigot.Data.GuiUtils.Gui;
import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer.JedisPlayer;
import me.whitehatd.BungeeCoreSpigot.Utilities.*;
import me.whitehatd.BungeeCoreSpigot.Utilities.Config.ConfigUtil;
import org.bukkit.Bukkit;
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
            JedisPlayer jedisPlayer = JedisPlayerManager.getByPlayer(p);
            jedisPlayer.refresh();
            SocialPreference pref = SocialPreference.valueOf(key.toUpperCase());
            String tog = jedisPlayer.socialStatus(pref)?"enabled":"disabled";
            ItemStack item;
            if(!key.equals("toggle_chat"))
            item = new ItemBuilder(Material.valueOf(ConfigUtil.str(prefix + ".toggle-item." + tog + ".type")))
                    .name(ConfigUtil.str(prefix + ".toggle-item." + tog + ".name")
                            .replaceAll("%type%", Utils.firstUpper(String.valueOf(pref).toLowerCase())))
                    .lore(ConfigUtil.arr(prefix + ".toggle-item." + tog + ".lore")).build();
            else item = new ItemBuilder(Material.valueOf(ConfigUtil.str(prefix + ".toggle-item-chat." + tog + ".type")))
                    .name(ConfigUtil.str(prefix + ".toggle-item-chat." + tog + ".name"))
                    .lore(ConfigUtil.arr(prefix + ".toggle-item-chat." + tog + ".lore")).build();
            addButton(slot, item, player -> {
                jedisPlayer.setSocialPreference(pref , !jedisPlayer.socialStatus(pref));
                SocialPreferenceChangeEvent s = new SocialPreferenceChangeEvent(jedisPlayer, pref, !jedisPlayer.socialStatus(pref));
                Bukkit.getPluginManager().callEvent(s);
                Utils.execSync(()-> {
                    new SocialPreferencesGUI().openGui(player);
                    ChatUtil.cs(player, "messages.pref-updated", str -> {
                        if(!key.equals("toggle_chat")) {
                            str = str.replaceAll("%type%", Utils.firstUpper(pref.name()))
                                    .replaceAll("%toggled%", (jedisPlayer.socialStatus(pref) ?
                                            ConfigUtil.str("messages.toggled.enabled")
                                            : ConfigUtil.str("messages.toggled.disabled")));
                        }else{
                            str = str.replaceAll("%type%", Utils.firstUpper(pref.name()))
                                    .replaceAll("%toggled%", (jedisPlayer.socialStatus(pref) ?
                                            ConfigUtil.str("messages.toggled-chat.enabled")
                                            : ConfigUtil.str("messages.toggled-chat.disabled")));
                        }
                        return str;
                    });
                });
            });
        }

    }

    @Override
    public void onClose(Player player) {

    }
}
