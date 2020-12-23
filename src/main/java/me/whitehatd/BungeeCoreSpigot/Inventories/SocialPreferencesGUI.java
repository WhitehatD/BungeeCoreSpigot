package me.whitehatd.BungeeCoreSpigot.Inventories;

import me.whitehatd.BungeeCoreSpigot.Data.ItemBuilder;
import me.whitehatd.BungeeCoreSpigot.Data.Preferences.SocialPreference;
import me.whitehatd.BungeeCoreSpigot.Events.Event.GameplayPreferenceChangeEvent;
import me.whitehatd.BungeeCoreSpigot.Events.Event.SocialPreferenceChangeEvent;
import me.whitehatd.BungeeCoreSpigot.GuiUtils.Gui;
import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer;
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
            JedisPlayer jedisPlayer = new JedisPlayer(p);
            SocialPreference pref = SocialPreference.valueOf(key.toUpperCase());
            String tog = jedisPlayer.socialStatus(pref)?"enabled":"disabled";
            ItemStack item = new ItemBuilder(Material.valueOf(ConfigUtil.str(prefix + ".toggle-item." + tog + ".type")))
                    .name(ConfigUtil.str(prefix + ".toggle-item." + tog + ".name")
                            .replaceAll("%type%", Utils.firstUpper(String.valueOf(pref).toLowerCase())))
                    .lore(ConfigUtil.arr(prefix + ".toggle-item." + tog + ".lore")).build();
            addButton(slot, item, player -> {
                jedisPlayer.setSocialPreference(pref , !jedisPlayer.socialStatus(pref));
                SocialPreferenceChangeEvent s = new SocialPreferenceChangeEvent(jedisPlayer, pref, !jedisPlayer.socialStatus(pref));
                Bukkit.getPluginManager().callEvent(s);
                Utils.execSync(()-> {
                    new SocialPreferencesGUI().openGui(player);
                    ChatUtil.cs(player, "messages.pref-updated", str ->
                            str.replaceAll("%type%", Utils.firstUpper(pref.name()))
                                    .replaceAll("%toggled%", (jedisPlayer.socialStatus(pref) ?
                                            ConfigUtil.str("messages.toggled.enabled")
                                            : ConfigUtil.str("messages.toggled.disabled"))));
                });
            });
        }

    }

    @Override
    public void onClose(Player player) {

    }
}
