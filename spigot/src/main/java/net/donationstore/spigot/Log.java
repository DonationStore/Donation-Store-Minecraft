package net.donationstore.spigot;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Log {

    public static void toConsole(String log) {
        System.out.println(String.format("[Donation Store]: %s", log));
    }

    public static void toPlayer(CommandSender sender, String log) {
        Player player = (Player) sender;
        player.sendMessage(String.format("%s[Donation Store]%s: %s", ChatColor.GREEN, ChatColor.WHITE, log));
    }
}
