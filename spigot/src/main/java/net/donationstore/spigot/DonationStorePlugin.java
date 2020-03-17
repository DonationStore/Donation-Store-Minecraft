package net.donationstore.spigot;

import net.donationstore.logging.Logging;

import net.donationstore.spigot.command.CommandHandler;
import net.donationstore.spigot.logging.Log;
import net.donationstore.spigot.queue.QueueTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DonationStorePlugin extends JavaPlugin {

    private Plugin plugin;
    private QueueTask queueTask;
    private FileConfiguration config;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {

        plugin = this;

        Log.toConsole(String.format(Logging.enableLog(), "Spigot"));

        config = plugin.getConfig();

        config.options().copyDefaults();

        saveConfig();

        queueTask = new QueueTask();
        commandHandler = new CommandHandler();

        queueTask.run(config, plugin);

    }

    public void runCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("ds")) {

            if (args.length == 0) {
                Log.send(sender, "Webstore and Helpdesk for Game Servers");
                Log.send(sender, "Spigot Plugin - Version 2.1");
                Log.send(sender, "https://donationstore.net");
                Log.send(sender, "Type /ds help for command information");
            } else {
                commandHandler.handleCommand(config, command, sender, plugin, args);
            }
        }

        return true;
    }

    @Override
    public void onDisable() {
        Log.toConsole("Stopping plugin, bye bye!");
    }
}
