package net.donationstore.sponge;

import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.ArrayList;

@Plugin(id = "donationstore", name = "Donation Store", version = "1.0", description = "The Sponge Plugin for Donation Store webstores")
public class DonationStorePlugin {

    private ArrayList<CommandSpec> commandSpecs;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        commandSpecs = new ArrayList<>();


    }

}
