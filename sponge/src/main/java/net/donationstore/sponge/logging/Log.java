package net.donationstore.sponge.logging;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class Log {

    public static void send(CommandSource sender, String log) {
        if (sender instanceof Player) {
            Text message = Text.builder("[Donation Store]").color(TextColors.GREEN)
                    .append(Text.builder(String.format(": %s", log)).color(TextColors.WHITE).build())
                    .build();

            sender.sendMessage(message);
        } else {
            Log.toConsole(String.format("[Donation Store]: %s", log));
        }
    }

    public static void toConsole(String log) {
        System.out.println(String.format("[Donation Store]: %s", log));
    }

    public static void displayLogs(CommandSource sender, List<String> logs) {
        for(String log: logs) {
            Log.send(sender, log);
        }
    }

}
