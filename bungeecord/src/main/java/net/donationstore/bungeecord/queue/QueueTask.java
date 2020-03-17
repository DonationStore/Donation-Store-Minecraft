package net.donationstore.bungeecord.queue;

import net.donationstore.bungeecord.logging.Log;
import net.donationstore.commands.CommandManager;
import net.donationstore.models.request.UpdateCommandExecutedRequest;
import net.donationstore.models.response.PaymentsResponse;
import net.donationstore.models.response.QueueResponse;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class QueueTask {

    private CommandManager commandManager;

    public void run(Configuration configuration, Plugin plugin) {
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if (configuration.getString("secret_key").isEmpty() || configuration.getString("webstore_api_location").isEmpty()) {
                    Log.toConsole("You must connect the plugin to your webstore before it can start executing purchased packages.");
                    Log.toConsole("Use /ds connect");
                } else {
                    commandManager = new CommandManager(configuration.getString("secret_key"), configuration.getString("webstore_api_location"));

                    try {
                        UpdateCommandExecutedRequest updateCommandExecutedRequest = new UpdateCommandExecutedRequest();

                        QueueResponse queueResponse = commandManager.getCommands();

                        for(PaymentsResponse payment: queueResponse.payments) {
                            for(net.donationstore.models.Command command: payment.commands) {

                                ProxiedPlayer onlinePlayer = plugin.getProxy().getPlayer(UUID.fromString(payment.meta.uuid));

                                if(onlinePlayer != null) {
                                    runCommand(plugin, command.command);
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
        }, 1, 45, TimeUnit.SECONDS);
    }

    public void runCommand(Plugin plugin, String command) {
        plugin.getProxy().getPluginManager().dispatchCommand(plugin.getProxy().getConsole(), command);
    }
}
