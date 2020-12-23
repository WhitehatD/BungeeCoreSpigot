package me.whitehatd.BungeeCoreSpigot.Utilities.Config;

import me.whitehatd.BungeeCoreSpigot.Utilities.ChatUtil;
import me.whitehatd.BungeeCoreSpigot.Utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtil {

    public static String str(String configSec){
        return ChatUtil.c(Utils.getConfig().getString(configSec));
    }

    public static Integer num(String configSec){
        return Utils.getConfig().getInt(configSec);
    }

    public static ArrayList<String> arr(String configSec){
        ArrayList<String> ls = new ArrayList<>();
        Utils.getConfig().getStringList(configSec).forEach(str -> ls.add(ChatUtil.c(str)));
        return ls;
    }



}
