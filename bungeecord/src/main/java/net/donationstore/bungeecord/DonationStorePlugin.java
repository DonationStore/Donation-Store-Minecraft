package net.donationstore.bungeecord;

import net.donationstore.bungeecord.commands.DonationStoreCommand;
import net.donationstore.bungeecord.config.FileConfiguration;
import net.donationstore.bungeecord.logging.Log;
import net.donationstore.bungeecord.queue.QueueTask;
import net.donationstore.exception.ClientException;
import net.donationstore.logging.Logging;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class DonationStorePlugin extends Plugin {

    private Plugin plugin;
    private QueueTask queueTask;
    private Configuration configuration;

    @Override
    public void onEnable() {
        plugin = this;

        queueTask = new QueueTask();

        Log.toConsole(String.format(Logging.enableLog(), "BungeeCord", "v2.2.3"));

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(FileConfiguration.loadResource(plugin, "config.yml"));

            if (configuration.getInt("queue_delay") == 0) {
                configuration.set("queue_delay", 180);
            }

            getProxy().getPluginManager().registerCommand(this, new DonationStoreCommand(configuration, plugin));

            queueTask.run(configuration, plugin);
        } catch(IOException exception) {
            throw new ClientException(String.format("Exception when starting Donation Store plugin: %s", exception.getMessage()));
        }
    }

    @Override
    public void onDisable() {
        Log.toConsole("Stopping plugin, bye bye!");
    }
}
