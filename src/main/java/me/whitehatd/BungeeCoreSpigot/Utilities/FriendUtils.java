package me.whitehatd.BungeeCoreSpigot.Utilities;

import me.whitehatd.BungeeCoreSpigot.BCS;
import me.whitehatd.BungeeCoreSpigot.Data.AsyncTask;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.*;

public class FriendUtils {

    public static String getRawRequests(){
        Future<String> future;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> task = () -> {
            try(Jedis jedis = BCS.jedisPool.getResource()){
                return jedis.get("friend-requests");
            }
        };
        future = executor.submit(task);
        try {
            return (future.get()!=null&&future.get().length()>0)?future.get():null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> getRawFriends(){
        Future<Map<String, String>> future;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Map<String, String>> task = () -> {
            try(Jedis jedis = BCS.jedisPool.getResource()){
                return jedis.hgetAll("friends");
            }
        };
        future = executor.submit(task);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendRequest(String sender, String target){
        String str = getRawRequests();
        String updatedRequests = "";
        boolean status = false;
        if(str!=null)
            for(String temp : str.split(",")){
                String section = temp.substring(1, temp.length()-1);
                String targetName = section.split(":")[0];
                if(targetName.equals(target)){
                    String reqArr = section.split(":")[1];
                    reqArr = reqArr.substring(1, reqArr.length()-1);
                    reqArr = reqArr + "@" + sender + "-" + System.currentTimeMillis();
                    updatedRequests = updatedRequests + "{" + targetName + ":[" + reqArr + "]},";
                    status = true;
                }else updatedRequests = updatedRequests + temp + ",";
            }
        if(!status){
            updatedRequests = updatedRequests + "{" + target + ":[" + sender+"-"+System.currentTimeMillis() + "]},";
        }
        String finalUpdatedRequests = updatedRequests;
        new AsyncTask(()->{
            try(Jedis jedis = BCS.jedisPool.getResource()){
                jedis.set("friend-requests", finalUpdatedRequests);
            }
        });
    }

    public static HashMap<String, Long> getRequests(String playerName){
        String str = getRawRequests();
        if(getRawRequests()!=null)
            for(String temp : str.split(",")) {
                String section = temp.substring(1, temp.length() - 1);
                String targetName = section.split(":")[0];
                if (targetName.equals(playerName)) {
                    String reqArr = section.split(":")[1];
                    reqArr = reqArr.substring(1, reqArr.length() - 1);
                    HashMap<String, Long> requests = new HashMap<>();
                    for(String s: reqArr.split("@")){
                        requests.put(s.split("-")[0], Long.parseLong(s.split("-")[1]));
                    }
                    return requests;
                }
            }
        return new HashMap<>();
    }

    public static void removeRequest(String requested, String player){
        String str = getRawRequests();
        String updatedRequests = "";
        if(str!=null)
            for(String temp : str.split(",")){
                String section = temp.substring(1, temp.length()-1);
                String targetName = section.split(":")[0];
                if(targetName.equals(requested)){
                    String reqArr = section.split(":")[1];
                    reqArr = reqArr.substring(1, reqArr.length()-1);
                    String newReqArr = "";
                    int i = 0;
                    for(String inviter : reqArr.split("@")){
                        if(!inviter.split("-")[0].equals(player)) {
                            newReqArr = inviter + "@";
                            i++;
                        }
                    }
                    if(i>0){
                        updatedRequests = updatedRequests + "{" + targetName + ":[" + newReqArr + "]},";
                    }
                }else updatedRequests = updatedRequests + temp + ",";
            }
        String finalUpdatedRequests = updatedRequests;
        new AsyncTask(()->{
            try(Jedis jedis = BCS.jedisPool.getResource()){
                jedis.set("friend-requests", finalUpdatedRequests);
            }
        });
    }

    public static ArrayList<String> getFriends(String playerName){
        if(getRawFriends()!=null)
            for(String name : getRawFriends().keySet()){
                if(name.equals(playerName)){
                    return new ArrayList<>(Arrays.asList(getRawFriends().get(name).split("@")));
                }
            }
        return new ArrayList<>();
    }

    public static void addFriend(String playerName, String friendName){
        String friends = "";
        Map<String, String> allFriends = new HashMap<>();
        if(getRawFriends()!=null){
            for(String s : getFriends(playerName))
                friends = friends + s + "@";
            friends = friends + friendName + "@";
            allFriends = getRawFriends();
            allFriends.put(playerName, friends);
        }else{
            allFriends.put(playerName, friendName+"@");
        }
        Map<String, String> finalAllFriends = allFriends;
        new AsyncTask(()->{
            try(Jedis jedis = BCS.jedisPool.getResource()){
                jedis.hmset("friends", finalAllFriends);
            }
        });
    }

    public static void removeFriend(String playerName, String friendName){
        String friends = "";
        Map<String, String> allFriends;
        if(getRawFriends()!=null) {
            for (String s : getFriends(playerName))
                if (!s.equals(friendName))
                    friends = friends + s + "@";
            allFriends = getRawFriends();
            allFriends.put(playerName, friends);
            Map<String, String> finalAllFriends = allFriends;
            new AsyncTask(() -> {
                try (Jedis jedis = BCS.jedisPool.getResource()) {
                    jedis.hmset("friends", finalAllFriends);
                }
            });
        }
    }

    public static Long getLastDisconnect(String playerName){
        Future<String> future;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String temp;
        Callable<String> task = () -> {
            try(Jedis jedis = BCS.jedisPool.getResource()){
                return jedis.hmget("playerdata", playerName).get(0).split("@")[0];
            }
        };
        future = executor.submit(task);
        try {
            return (future.get()!=null&&future.get().length()>0)?Long.parseLong(future.get()):null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean isOnline(String playerName){
        Future<String> future;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String temp;
        Callable<String> task = () -> {
            try(Jedis jedis = BCS.jedisPool.getResource()){
                return jedis.hmget("playerdata", playerName).get(0).split("@")[1];
            }
        };
        future = executor.submit(task);
        try {
            if(future.get()!=null&&future.get().length()>0){
                return future.get().equals("online");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
