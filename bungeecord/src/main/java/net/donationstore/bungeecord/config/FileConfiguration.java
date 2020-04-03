package net.donationstore.bungeecord.config;

import net.donationstore.exception.ClientException;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public class FileConfiguration {

    public static File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
            }
        } catch (Exception exception) {
            throw new ClientException(String.format("Exception when creating a Donation Store configuration %s", exception.getMessage()));
        }
        return resourceFile;
    }
}
