package me.whitehatd.BungeeCoreSpigot.Inventories;

import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer.JedisPlayerManager;
import me.whitehatd.BungeeCoreSpigot.Data.Preferences.GameplayPreference;
import me.whitehatd.BungeeCoreSpigot.Data.ItemBuilder;
import me.whitehatd.BungeeCoreSpigot.Data.JedisPlayer.JedisPlayer;
import me.whitehatd.BungeeCoreSpigot.Events.Event.GameplayPreferenceChangeEvent;
import me.whitehatd.BungeeCoreSpigot.Data.GuiUtils.Gui;
import me.whitehatd.BungeeCoreSpigot.Utilities.ChatUtil;
import me.whitehatd.BungeeCoreSpigot.Utilities.Config.ConfigUtil;
import me.whitehatd.BungeeCoreSpigot.Utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GameplayPreferencesGUI extends Gui {

    private final String prefix = "prefs-inventory.gameplay-prefs";

    public GameplayPreferencesGUI() {
        super(ConfigUtil.str("prefs-inventory.gameplay-prefs" + ".name"),
                ConfigUtil.num("prefs-inventory.gameplay-prefs" + ".size")/9);
    }

    @Override
    public void setupGui(Player p) {
        for(String key : Utils.getConfig().getConfigurationSection(prefix + ".items").getKeys(false)){
            int slot = ConfigUtil.num(prefix + ".items." + key + ".slot");
            JedisPlayer jedisPlayer = JedisPlayerManager.getByPlayer(p);
            jedisPlayer.refresh();
            GameplayPreference pref = GameplayPreference.valueOf(key.toUpperCase());
            String tog = jedisPlayer.gameplayStatus(pref)?"enabled":"disabled";
            ItemStack item = new ItemBuilder(Material.valueOf(ConfigUtil.str(prefix + ".toggle-item." + tog + ".type")))
                    .name(ConfigUtil.str(prefix + ".toggle-item." + tog + ".name")
                            .replaceAll("%type%", Utils.firstUpper(String.valueOf(pref).toLowerCase())))
                    .lore(ConfigUtil.arr(prefix + ".toggle-item." + tog + ".lore")).build();
            addButton(slot, item, player -> {
                jedisPlayer.setGameplayPreference(pref , !jedisPlayer.gameplayStatus(pref));
                GameplayPreferenceChangeEvent g = new GameplayPreferenceChangeEvent(jedisPlayer, pref, !jedisPlayer.gameplayStatus(pref));
                Bukkit.getPluginManager().callEvent(g);
                Utils.execSync(()-> {
                    new GameplayPreferencesGUI().openGui(player);
                    ChatUtil.cs(player, "messages.pref-updated", str ->
                            str.replaceAll("%type%", Utils.firstUpper(pref.name()))
                                    .replaceAll("%toggled%", (jedisPlayer.gameplayStatus(pref) ?
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
