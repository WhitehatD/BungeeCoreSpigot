package me.whitehatd.BungeeCoreSpigot.Utilities;


import me.whitehatd.BungeeCoreSpigot.Utilities.Config.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {


    public static String c(String message)
    {
        char COLOR_CHAR = '\u00A7';
        final Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static void s(Player player, String message){
        player.sendMessage(c(message));
    }

    public static void cs(Player player, String configSec, Function<String, String> addPlaceholders){
        String str = c(ConfigUtil.str(configSec));
        player.sendMessage(addPlaceholders.apply(str));
    }

    public static void cs(Player player, String configSec){
        player.sendMessage(c(ConfigUtil.str(configSec)));
    }



}
