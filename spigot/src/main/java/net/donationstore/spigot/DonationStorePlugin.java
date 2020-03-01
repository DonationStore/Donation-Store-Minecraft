package net.donationstore.spigot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.donationstore.commands.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class DonationStorePlugin extends JavaPlugin {

    private Plugin plugin;
    private FileConfiguration config;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        Log.toConsole("Starting plugin...");
        Log.toConsole("For Support/Help, Please Visit: https://donationstore.net/support");
        config = plugin.getConfig();

        commandManager = new CommandManager();

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            public void run() {
                try {
                    JsonArray commandQueue = commandManager.getCommands("secretKey", "webstoreAPILocation");

                    for (JsonElement element: commandQueue) {
                        JsonObject payment = element.getAsJsonObject();

                        JsonObject meta = payment.get("meta").getAsJsonObject();

                        Player player = Bukkit.getPlayer(UUID.fromString(payment.get("uuid").getAsString()));

                        if (player != null) {
                            // Execute commands
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1, 5000);
    }
}
