package net.donationstore.sponge.config;

import com.google.inject.Inject;
import net.donationstore.exception.ClientException;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.config.ConfigDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileConfiguration {

    private Path configurationFile;
    private CommentedConfigurationNode configurationNode;
    private ConfigurationLoader<CommentedConfigurationNode> configurationLoader;

    public FileConfiguration(Path configurationDir) {
        configurationFile = Paths.get(configurationDir + "/config.conf");
        configurationLoader = HoconConfigurationLoader.builder().setPath(configurationFile).build();

        try {
            if(!Files.exists(configurationDir)) {
                Files.createDirectories(configurationDir);
            }

            if(!Files.exists(configurationFile)) {
                Files.createFile(configurationFile);
            } else {
                configurationNode = configurationLoader.load();
            }

            if(configurationNode == null) {
                configurationNode = configurationLoader.createEmptyNode(ConfigurationOptions.defaults());
            }
        } catch(IOException exception) {
            throw new ClientException(String.format("Exception when creating a Donation Store configuration %s", exception.getMessage()));
        }
    }

    public CommentedConfigurationNode getNode(String node) {
        return configurationNode.getNode(node);
    }

    public void save() {
        try {
            configurationLoader.save(configurationNode);
        } catch(IOException exception) {
                throw new ClientException(String.format("Exception when creating a Donation Store configuration %s", exception.getMessage()));
        }
    }
}
