package net.donationstore.sponge;

import com.google.inject.Inject;
import net.donationstore.logging.Logging;
import net.donationstore.sponge.config.FileConfiguration;
import net.donationstore.sponge.queue.QueueTask;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;

import java.nio.file.Path;
import java.util.ArrayList;

@Plugin(id = "donationstore", name = "Donation Store", version = "2.1", description = "The Sponge Plugin for Donation Store webstores")
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
        queueTask = new QueueTask();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info(String.format(Logging.enableLog(), "Sponge"));

        fileConfiguration.getNode("secret-key").setValue("FHLRFHQ1BI1NQ3JWZ24I090AZ374QJ825PWPBWLGCYEQR6FREI1I4U1NORW6UB36");
        fileConfiguration.getNode("webstore-api-location").setValue("http://f06f24fc.ngrok.io/api");
        fileConfiguration.save();
        queueTask.run(fileConfiguration, pluginContainer);
    }

}
