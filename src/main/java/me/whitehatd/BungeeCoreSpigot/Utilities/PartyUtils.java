package me.whitehatd.BungeeCoreSpigot.Utilities;

import me.whitehatd.BungeeCoreSpigot.BCS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class PartyUtils {

    public static List<String> getAllParties(){
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
        return Arrays.asList(parties.split("@"));
    }

    public static String hasPartyChat(Player player){
        ArrayList<String> leaders = new ArrayList<>();
        String partyChat = "no";
        String leader, members;
        if(getAllParties().isEmpty()){
            return "no";
        }
        for(String party : getAllParties()){
            leader = party.substring(1).split("!")[0].substring(0, party.split("!")[0].indexOf("[")-1);
            members = party.split("!")[1].substring(0, party.split("!")[1].indexOf(")"));
            if(members.length()>1) {
                for (String member : members.split("\\$")) {
                    String finalMember = member.substring(0, member.indexOf("["));
                    if (finalMember.equals(player.getName())) {
                        partyChat = member.substring(member.indexOf("[") + 1, member.indexOf("]"));
                    }
                }
            }
            if(leader.equals(player.getName())){
                partyChat = party.substring(1).split("!")[0].substring(
                        party.split("!")[0].indexOf("["), party.split("!")[0].indexOf("]")-1);
            }
        }
        return partyChat;
    }


}
