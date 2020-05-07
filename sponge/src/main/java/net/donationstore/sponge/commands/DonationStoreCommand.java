package net.donationstore.sponge.commands;

import net.donationstore.commands.CommandFactory;
import net.donationstore.sponge.config.FileConfiguration;
import net.donationstore.sponge.logging.Log;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class DonationStoreCommand implements CommandCallable {

    private CommandFactory commandFactory;
    private FileConfiguration fileConfiguration;

    public DonationStoreCommand(FileConfiguration configuration) {
        this.fileConfiguration = configuration;
        this.commandFactory = new CommandFactory();
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        String[] args;
        if(arguments.isEmpty()) {
            args = new String[]{};
        } else {
            args = arguments.split(" ");
        }
        if(args.length < 1) {
            Log.send(source, "Webstore and Helpdesk for Game Servers");
            Log.send(source, "Sponge Plugin - Version 2.1");
            Log.send(source, "https://donationstore.net");
            Log.send(source, "Type /ds help for command information");
        } else {
            if (fileConfiguration.getNode("secret-key").getValue() == null || fileConfiguration.getNode("webstore-api-location").getValue() == null) {
                if (args[0].equals("connect")) {
                    if (source instanceof Player) {
                        Log.send(source, "For security reasons, that command can only be executed from the console.");
                    } else {
                        try {
                            Log.displayLogs(source, commandFactory.getCommand(args).runCommand());

                            fileConfiguration.getNode("secret-key").setValue(args[1]);
                            fileConfiguration.getNode("webstore-api-location").setValue(args[2]);

                            fileConfiguration.save();
                        } catch(Exception exception) {
                            Log.send(source, exception.getMessage());
                        }
                    }
                }
            } else {
                try {
                    List<String> listOfArgs = new ArrayList<>();

                    listOfArgs.add(args[0]);
                    listOfArgs.add(fileConfiguration.getNode("secret-key").getString());
                    listOfArgs.add(fileConfiguration.getNode("webstore-api-location").getString());

                    Collections.addAll(listOfArgs, Arrays.copyOfRange(args, 1, args.length));

                    if (source instanceof Player) {
                        listOfArgs.add(((Player) source).getUniqueId().toString());

                        if (source.hasPermission(commandFactory.getCommand(listOfArgs.toArray(new String[0])).getPermission())) {
                            Log.displayLogs(source, commandFactory.getCommand(listOfArgs.toArray(new String[0])).runCommand());
                        } else {
                            Log.send(source, "You don't have permission to execute that command.");
                        }
                    }
                } catch(Exception exception) {
                    Log.send(source, exception.getMessage());
                }
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return false;
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public Text getUsage(CommandSource source) {
        return null;
    }
}
