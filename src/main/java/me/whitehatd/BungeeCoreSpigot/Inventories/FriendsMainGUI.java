package me.whitehatd.BungeeCoreSpigot.Inventories;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import me.whitehatd.BungeeCoreSpigot.BCS;
import me.whitehatd.BungeeCoreSpigot.Utilities.Config.ConfigUtil;
import me.whitehatd.BungeeCoreSpigot.Utilities.FriendUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class FriendsMainGUI {

    public static Inventory get(Player player){
        String sub = "main-friends-inventory";
        SGMenu menu = BCS.spiGUI.create(ConfigUtil.str(sub+".name"),
                ConfigUtil.num(sub+".size")/9);
        SGButton friendReq = new SGButton(new ItemBuilder(Material.
                valueOf(ConfigUtil.str(sub + ".friend-req-item.type")))
                .lore(ConfigUtil.arr(sub+".friend-req-item.lore"))
                .amount(1)
                .name(ConfigUtil.str(sub+ ".friend-req-item.name")).build())
                .withListener((InventoryClickEvent e)-> {
                    //TODO: ADD FRIEND REQUESTS INVENTORY HERE
                });
        menu.setButton(ConfigUtil.num(sub+".friend-req-item.slot"), friendReq);
        menu.stickSlot(ConfigUtil.num(sub+".friend-req-item.slot"));
        int count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getDefault());
        for(String friend: FriendUtils.getFriends(player.getName())){
                ArrayList<String> lore = ConfigUtil.arr(sub + ".friend.lore");
                if(friend.length()<1)continue;
                Date date = new Date(FriendUtils.getLastDisconnect(friend));
                lore.replaceAll(c -> c.replaceAll("%online%",
                        FriendUtils.isOnline(friend)?"&aonline":sdf.format(date)));
                SGButton friendButton = new SGButton(new ItemBuilder(Material.PLAYER_HEAD).skullOwner(friend)
                        .lore(lore)
                        .amount(1)
                        .name(ConfigUtil.str(sub + ".friend.name").replaceAll("%player%", friend))
                        .build())
                        .withListener((InventoryClickEvent e) -> {
                            //TODO: FRIEND INFO
                        });
                menu.setButton(menu.getCurrentPage(), count + 9, friendButton);
            count ++;
        }
        return menu.getInventory();
    }
}
