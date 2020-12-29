package me.whitehatd.BungeeCoreSpigot.Utilities;

import me.whitehatd.BungeeCoreSpigot.BCS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

public class PartyUtils {

    public static String[] getAllParties(){
        Future<String> future;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> part = () -> {
            try(Jedis jedis = BCS.jedisPool.getResource()) {
                return jedis.get("parties");
            }
        };
        future = executor.submit(part);
        String parties = null;
        try {
            parties = future.get();
        } catch (InterruptedException | ExecutionException interruptedException) {
            interruptedException.printStackTrace();
        }
        return parties.split("@");
    }

    public static ArrayList<String> getLeaders(){
        ArrayList<String> leaders = new ArrayList<>();
        for(String party : getAllParties()){
            leaders.add(party.split("!")[0]);
        }
        return leaders;
    }

    public static ArrayList<String> getMembers(){
        ArrayList<String> members = new ArrayList<>();
        for(String party : getAllParties()){
            for(String member: party.split("!")[1].substring(0, party.split("!")[1].length()-1).split("\\$")){
                members.add(member);
            }
        }
        return members;
    }

}
