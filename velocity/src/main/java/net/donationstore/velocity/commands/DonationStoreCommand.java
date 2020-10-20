package net.donationstore.velocity.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.donationstore.commands.CommandFactory;
import net.donationstore.velocity.config.FileConfiguration;
import net.donationstore.velocity.logging.Log;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DonationStoreCommand implements Command {

    private FileConfiguration fileConfiguration;
    private CommandFactory commandFactory;

    public DonationStoreCommand(FileConfiguration configuration) {
        this.fileConfiguration = configuration;
        this.commandFactory = new CommandFactory();
    }

    @Override
    public void execute(@NonNull CommandSource commandSender, String[] strings) {
        if(strings.length < 1) {
            Log.send(commandSender, "Webstore and Helpdesk for Game Servers");
            Log.send(commandSender, "Velocity Plugin - Version 2.2");
            Log.send(commandSender, "https://donationstore.net");
            Log.send(commandSender, "Type /ds help for command information");
        } else {
            if (fileConfiguration.getNode("secret-key").getValue() == null || fileConfiguration.getNode("webstore-api-location").getValue() == null) {
                if (strings[0].equals("connect")) {
                    if (commandSender instanceof Player) {
                        Log.send(commandSender, "For security reasons, that command can only be executed from the console.");
                    } else {
                        try {
                            Log.displayLogs(commandSender, commandFactory.getCommand(strings).runCommand());

                            fileConfiguration.getNode("secret-key").setValue(strings[1]);
                            fileConfiguration.getNode("webstore-api-location").setValue(strings[2]);

                            fileConfiguration.save();
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
                    listOfArgs.add(fileConfiguration.getNode("secret-key").getString());
                    listOfArgs.add(fileConfiguration.getNode("webstore-api-location").getString());

                    Collections.addAll(listOfArgs, Arrays.copyOfRange(strings, 1, strings.length));

                    if (commandSender instanceof Player) {
                        listOfArgs.add(((Player) commandSender).getUniqueId().toString());

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
