package net.donationstore.spigot;

import net.donationstore.commands.CommandFactory;
import net.donationstore.commands.CommandManager;
import net.donationstore.dto.CommandExectionPayloadDTO;
import net.donationstore.dto.QueueDTO;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Map;

public class DonationStorePlugin extends JavaPlugin {

    private Plugin plugin;
    private FileConfiguration config;
    private CommandManager commandManager;

    private CommandFactory commandFactory;

    @Override
    public void onEnable() {

        commandFactory = new CommandFactory();

        Log.toConsole("Starting plugin...");
        Log.toConsole("For Support/Help, Please Visit: https://donationstore.net/support");
        config = plugin.getConfig();

        commandManager = new CommandManager();

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            public void run() {
                try {
                    ArrayList<String> executedCommands = new ArrayList<>();
                    // Get the command queue, execute the commands that come back.
                    // Only active commands will come back.
                    // Keep a list of the executed commands and then POST them back using the command managers update method.
                    QueueDTO queueDTO = commandManager.getCommands("secretKey", "webstoreAPILocation");

                    // Execute commands
                    for(CommandExectionPayloadDTO commandPayload: queueDTO.commandExectionPayloadDTO) {
                        for(Map.Entry<String, String> command: commandPayload.commands.entrySet()) {
                            if (!Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.getValue())) {
                                executedCommands.add(command.getKey());
                            }
                        }
                    }
                    // Make request back with executed commands
                    /*if (commandManager.updateCommandsToExecuted("secretKey", "webstoreAPILocation", executedCommands)) {
                        // Do something
                    }*/
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1, 5000);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("ds")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Log.toPlayer(sender, "Webstore and Helpdesk for Game Servers");
                    Log.toPlayer(sender, "Spigot Plugin - Version 2.1");
                    Log.toPlayer(sender, "https://donationstore.net");
                    Log.toPlayer(sender, "Type /ds help for command information");
                }
            } else {
                // Call execute command method
                // The actual args array will have the variables we need.
                // BUT, we do need to put the webstoreAPILocation and secrety key as number 1 and 2
                // The rest are whatever.
                try {
                    /* The following isn't possible because we need to push the secret key
                       and webstore API location to the front of the args
                     */
                    // commandFactory.getCommand(args).runCommand();
                    ArrayList<String> arrayListOfArgs = new ArrayList<>();

                    String[] secretAndWebstore = {"secretKey", "webstoreAPILocation"};
                    String[] argsForCommand = new String[secretAndWebstore.length + args.length];
                    System.arraycopy(secretAndWebstore, 0, argsForCommand, 1, secretAndWebstore.length);


                    commandFactory.getCommand(argsForCommand).runCommand();


                    commandManager.executeCommand(args);
                } catch(Exception exception) {
                    if (sender instanceof Player) {
                        Log.toPlayer(sender, exception.getMessage());
                    } else {
                        Log.toConsole(exception.getMessage());
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void onDisable() {
        Log.toConsole("Stopping plugin, bye bye!");
    }

    // Override onCommand method
}
