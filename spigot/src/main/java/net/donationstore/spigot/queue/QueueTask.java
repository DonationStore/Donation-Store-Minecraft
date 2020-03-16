package net.donationstore.spigot.queue;

import net.donationstore.commands.CommandManager;
import net.donationstore.models.request.UpdateCommandExecutedRequest;
import net.donationstore.models.response.PaymentsResponse;
import net.donationstore.models.response.QueueResponse;
import net.donationstore.spigot.Log;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class QueueTask {

    private CommandManager commandManager;

    public void run(FileConfiguration config, Plugin plugin) {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
            public void run() {
                if (config.getString("secret_key") == null || config.getString("webstore_api_location") == null) {
                    Log.toConsole("You must connect the plugin to your webstore before it can start executing purchased packages.");
                    Log.toConsole("Use /ds connect");
                } else {
                    commandManager = new CommandManager(config.getString("secret_key"), config.getString("webstore_api_location"));
                    try {
                        UpdateCommandExecutedRequest updateCommandExecutedRequest = new UpdateCommandExecutedRequest();

                        QueueResponse queueResponse = commandManager.getCommands();

                        for(PaymentsResponse payment: queueResponse.payments) {
                            for(net.donationstore.models.Command command: payment.commands) {

                                Player onlinePlayer = Bukkit.getPlayer(UUID.fromString(payment.meta.uuid));
                                if(onlinePlayer != null) {
                                    Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            runCommand(command.command);
                                        }
                                    });
                                    updateCommandExecutedRequest.getCommands().add(command.id);
                                }
                            }
                        }
                        commandManager.updateCommandsToExecuted(updateCommandExecutedRequest);
                    } catch(Exception e) {
                        Log.toConsole(e.getMessage());
                    }
                }
            }
        }, 1, 750);
    }

    public void runCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
