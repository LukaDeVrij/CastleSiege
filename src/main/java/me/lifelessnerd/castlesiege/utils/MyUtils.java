package me.lifelessnerd.castlesiege.utils;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MyUtils {

    public static void broadcastGroup(ArrayList<Player> playerGroup, String string){
        for (Player player : playerGroup){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));

        }
    }

    public static void sendColorMessage(Player player, String string){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));

    }
}
