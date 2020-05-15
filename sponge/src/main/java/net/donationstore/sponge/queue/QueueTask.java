package net.donationstore.sponge.queue;

import net.donationstore.commands.CommandManager;
import net.donationstore.models.request.UpdateCommandExecutedRequest;
import net.donationstore.models.response.PaymentsResponse;
import net.donationstore.models.response.QueueResponse;
import net.donationstore.sponge.config.FileConfiguration;
import net.donationstore.sponge.logging.Log;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class QueueTask {

    private Task.Builder taskBuilder;

    public QueueTask() {
        taskBuilder = Task.builder();
    }

    public void run(FileConfiguration configuration, PluginContainer pluginContainer) {
        taskBuilder.execute(() -> {
            if (configuration.getNode("secret-key").getValue() == null || configuration.getNode("webstore-api-location").getValue() == null) {
                Log.toConsole("You must connect the plugin to your webstore before it can start executing purchased packages.");
                Log.toConsole("Use /ds connect");
            } else {
                CommandManager commandManager = new CommandManager(configuration.getNode("secret-key").getString(), configuration.getNode("webstore-api-location").getString());
                SpongeExecutorService syncExecutor = Sponge.getScheduler().createSyncExecutor(pluginContainer);

                try {
                    UpdateCommandExecutedRequest updateCommandExecutedRequest = new UpdateCommandExecutedRequest();

                    QueueResponse queueResponse = commandManager.getCommands();

                    for(PaymentsResponse payment: queueResponse.payments) {
                        for(net.donationstore.models.Command command: payment.commands) {

                            Optional<Player> player = Sponge.getServer().getPlayer(UUID.fromString(payment.meta.uuid));

                            if (player.isPresent()) {
                                syncExecutor.submit(() -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command.command));
                                updateCommandExecutedRequest.getCommands().add(command.id);
                            }

                        }
                    }
                    commandManager.updateCommandsToExecuted(updateCommandExecutedRequest);
                } catch(Exception e) {
                    Log.toConsole(e.getMessage());
                }
            }
        }).async().delay(20, TimeUnit.SECONDS).interval(4, TimeUnit.MINUTES).submit(pluginContainer);
    }
}
