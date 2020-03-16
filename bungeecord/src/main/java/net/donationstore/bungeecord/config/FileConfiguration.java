package net.donationstore.bungeecord.config;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }
}
