package com.balz3.tictactoe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Message {

    public static void send(CommandSender sender, String message){
        send(sender, message, "&a");
    }

    public static void send(String sender, String message){
        send(sender, message, "&a");
    }

    public static void send(CommandSender sender, String message, String prefix){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static void send(String sender, String message, String prefix){
        if(sender.equalsIgnoreCase("operator")){
            System.out.println(message);
        } else if(Bukkit.getServer().getPlayerExact(sender) != null){
            Bukkit.getServer().getPlayerExact(sender).sendMessage(
                    ChatColor.translateAlternateColorCodes('&', prefix + message));
        }

    }

}
