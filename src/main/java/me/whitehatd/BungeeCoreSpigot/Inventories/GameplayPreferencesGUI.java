package me.whitehatd.BungeeCoreSpigot.Inventories;

import me.whitehatd.BungeeCoreSpigot.GuiUtils.Gui;
import me.whitehatd.BungeeCoreSpigot.Utilities.ConfigUtil;
import org.bukkit.entity.Player;

public class GameplayPreferencesGUI extends Gui {

    private final String prefix = "prefs-inventory.gameplay-prefs";

    public GameplayPreferencesGUI() {
        super(ConfigUtil.str("prefs-inventory.gameplay-prefs" + ".name"),
                ConfigUtil.num("prefs-inventory.gameplay-prefs" + ".size")/9);
    }

    @Override
    public void setupGui(Player p) {

    }

    @Override
    public void onClose(Player player) {

    }
}
