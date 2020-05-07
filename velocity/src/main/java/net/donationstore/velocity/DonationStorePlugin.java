package net.donationstore.velocity;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.donationstore.exception.ClientException;
import net.donationstore.logging.Logging;
import net.donationstore.velocity.commands.DonationStoreCommand;
import net.donationstore.velocity.config.FileConfiguration;
import net.donationstore.velocity.logging.Log;
import net.donationstore.velocity.queue.QueueTask;

import java.nio.file.Path;

@Plugin(id = "donationstore", name = "Donation-Store-Velocity", version = "2.1", authors = {"Donation Store"})
public class DonationStorePlugin {

    private ProxyServer server;
    private QueueTask queueTask;
    private Path dataDirectory;
    private FileConfiguration fileConfiguration;

    @Inject
    public DonationStorePlugin(ProxyServer server, @DataDirectory Path dataDirectory) {
        this.server = server;

        this.queueTask = new QueueTask();

        this.dataDirectory = dataDirectory;

        Log.toConsole(String.format(Logging.enableLog(), "Velocity"));
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            fileConfiguration = new FileConfiguration(dataDirectory);

            server.getCommandManager().register(new DonationStoreCommand(fileConfiguration), "ds");

            queueTask.run(fileConfiguration, this);
        } catch(Exception exception) {
            throw new ClientException(String.format("Exception when starting Donation Store plugin: %s", exception.getMessage()));
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        Log.toConsole("Stopping plugin, bye bye!");
    }

    public ProxyServer getServer() {
        return this.server;
    }
}
