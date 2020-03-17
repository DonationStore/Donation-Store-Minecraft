package net.donationstore.spigot.logging;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Log {

    public static void send(CommandSender sender, String log) {
        if (sender instanceof Player) {
            sender.sendMessage(String.format("%s[Donation Store]%s: %s", ChatColor.GREEN, ChatColor.WHITE, log));
        } else {
            Log.toConsole(String.format("[Donation Store]: %s", log));
        }
    }

    public static void toConsole(String log) {
        System.out.println(String.format("[Donation Store]: %s", log));
    }

    public static void displayLogs(CommandSender sender, List<String> logs) {
        for(String log: logs) {
            Log.send(sender, log);
        }
    }
}
