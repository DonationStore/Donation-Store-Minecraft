package net.donationstore.bungeecord;

import com.google.common.io.ByteStreams;
import net.donationstore.bungeecord.commands.DonationStoreCommand;
import net.donationstore.bungeecord.config.FileConfiguration;
import net.donationstore.bungeecord.queue.QueueTask;
import net.donationstore.commands.CommandFactory;
import net.donationstore.commands.CommandManager;
import net.donationstore.exception.ClientException;
import net.donationstore.logging.Logging;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class DonationStorePlugin extends Plugin {

    private Plugin plugin;
    private Configuration configuration;
    private CommandManager commandManager;

    private QueueTask queueTask;
    private CommandFactory commandFactory;

    @Override
    public void onEnable() {
        plugin = this;

        queueTask = new QueueTask();

        Log.toConsole(String.format(Logging.enableLog(), "BungeeCord"));

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(FileConfiguration.loadResource(plugin, "config.yml"));

            getProxy().getPluginManager().registerCommand(this, new DonationStoreCommand(configuration, plugin));

            queueTask.run(configuration, plugin);
        } catch(IOException exception) {
            throw new ClientException(String.format("Exception when starting Donation Store plugin: %s", exception.getMessage()));
        }
    }
}
