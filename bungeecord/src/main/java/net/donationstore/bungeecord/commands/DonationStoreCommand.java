package net.donationstore.bungeecord.commands;

import net.donationstore.bungeecord.logging.Log;
import net.donationstore.commands.CommandFactory;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DonationStoreCommand extends Command {

    private Plugin plugin;
    private Configuration configuration;
    private CommandFactory commandFactory;

    public DonationStoreCommand(Configuration configuration, Plugin plugin) {
        super("ds");
        commandFactory = new CommandFactory();
        this.configuration = configuration;
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(strings.length < 1) {
            Log.send(commandSender, "Webstore and Helpdesk for Game Servers");
            Log.send(commandSender, "BungeeCord Plugin - Version 2.3");
            Log.send(commandSender, "https://donationstore.net");
            Log.send(commandSender, "Type /ds help for command information");
        } else {
            if (configuration.getString("secret_key").isEmpty() || configuration.getString("webstore_api_location").isEmpty()) {
                if (strings[0].equals("connect")) {
                    if (commandSender instanceof ProxiedPlayer) {
                        Log.send(commandSender, "For security reasons, that command can only be executed from the console.");
                    } else {
                        try {
                            Log.displayLogs(commandSender, commandFactory.getCommand(strings).runCommand());

                            configuration.set("secret_key", strings[1]);
                            configuration.set("webstore_api_location", strings[2]);

                            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(plugin.getDataFolder(), "config.yml"));
                        } catch(Exception exception) {
                            Log.send(commandSender, exception.getMessage());
                        }
                    }
                } else {
                    Log.send(commandSender,"Cannot run commands as this plugin has not yet been setup.");
                }
            } else {
                try {
                    List<String> listOfArgs = new ArrayList<>();

                    listOfArgs.add(strings[0]);
                    listOfArgs.add(configuration.getString("secret_key"));
                    listOfArgs.add(configuration.getString("webstore_api_location"));

                    Collections.addAll(listOfArgs, Arrays.copyOfRange(strings, 1, strings.length));

                    if (commandSender instanceof ProxiedPlayer) {
                        listOfArgs.add(((ProxiedPlayer) commandSender).getUniqueId().toString());

                        if (commandSender.hasPermission(commandFactory.getCommand(listOfArgs.toArray(new String[0])).getPermission())) {
                            Log.displayLogs(commandSender, commandFactory.getCommand(listOfArgs.toArray(new String[0])).runCommand());
                        } else {
                            Log.send(commandSender, "You don't have permission to execute that command.");
                        }
                    } else {
                        Log.displayLogs(commandSender, commandFactory.getCommand(listOfArgs.toArray(new String[0])).runCommand());
                    }
                } catch(Exception exception) {
                    Log.send(commandSender, exception.getMessage());
                }
            }
        }
    }
}
