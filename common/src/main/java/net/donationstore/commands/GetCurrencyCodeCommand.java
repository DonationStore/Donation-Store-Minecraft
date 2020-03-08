package net.donationstore.commands;

import net.donationstore.dto.WebstoreAPIResponseDTO;
import net.donationstore.exception.InvalidCommandUseException;

import java.util.ArrayList;

public class GetCurrencyCodeCommand extends AbstractApiCommand {
    
    private String username;

    @Override
    public String getSupportedCommand() {
        return "code";
    }

    @Override
    public Command validate(String[] args) {
        if (args.length != 3) {
            getLogs().add(getInvalidCommandMessage());
            getLogs().add(helpInfo());
            throw new InvalidCommandUseException(getLogs());
        }

        getWebstoreHTTPClient().setSecretKey(args[0])
                .setWebstoreAPILocation(args[1]);

        setUsername(args[3]);
        return this;
    }

    @Override
    public ArrayList<String> runCommand() throws Exception {

        WebstoreAPIResponseDTO webstoreAPIResponseDTO = getWebstoreHTTPClient().post(this, "currency/code/generate");

        // Do stuff with the body
        return getLogs();
    }

    @Override
    public String helpInfo() {
        return "This command is used to generate a Virtual Currency Claim code used on a webstore to pay with Virtual Currencies.";
    }

    @Override
    public CommandType commandType() {
        return CommandType.PLAYER;
    }

    public String getUsername() {
        return username;
    }

    public GetCurrencyCodeCommand setUsername(String username) {
        this.username = username;
        return this;
    }
}
