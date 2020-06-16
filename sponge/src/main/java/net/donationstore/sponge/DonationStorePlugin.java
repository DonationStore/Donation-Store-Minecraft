package net.donationstore.sponge;

import com.google.inject.Inject;
import net.donationstore.logging.Logging;
import net.donationstore.sponge.commands.DonationStoreCommand;
import net.donationstore.sponge.config.FileConfiguration;
import net.donationstore.sponge.logging.Log;
import net.donationstore.sponge.queue.QueueTask;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;

import java.nio.file.Path;
import java.util.ArrayList;

@Plugin(id = "donationstore", name = "Donation Store", version = "2.2.3", description = "The Sponge Plugin for Donation Store webstores")
public class DonationStorePlugin {

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configurationDir;

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer pluginContainer;

    private QueueTask queueTask;

    private FileConfiguration fileConfiguration;

    private ArrayList<CommandSpec> commandSpecs;

    private PluginManager pluginManager = Sponge.getPluginManager();

    @Listener
    public void onGamePreInitializationEvent(GamePreInitializationEvent event) {
        fileConfiguration = new FileConfiguration(configurationDir);

        if (fileConfiguration.getNode("queue-delay").getInt() == 0) {
            fileConfiguration.getNode("queue-delay").setValue(180);
            fileConfiguration.save();
        }

        queueTask = new QueueTask();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info(String.format(Logging.enableLog(), "Sponge", "v2.2.3"));

        Sponge.getCommandManager().register(pluginContainer, new DonationStoreCommand(fileConfiguration), "ds");

        queueTask.run(fileConfiguration, pluginContainer);
    }

    @Listener
    public void onServerStop(GameStoppedEvent event) {
        Log.toConsole("Stopping plugin, bye bye!");
    }

}
