package net.donationstore.commands;

import net.donationstore.dto.WebstoreAPIResponseDTO;
import net.donationstore.exception.InvalidCommandUseException;

import java.util.ArrayList;

public class ConnectCommand extends AbstractApiCommand {

    @Override
    public String getSupportedCommand() {
        return "connect";
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

        return this;
    }

    @Override
    public ArrayList<String> runCommand() throws Exception {

        WebstoreAPIResponseDTO webstoreAPIResponseDTO = getWebstoreHTTPClient().get(this, "information");

        // Do stuff with the body
        return getLogs();
    }

    @Override
    public String helpInfo() {
        return "This command is used to connect the Donation Store plugin to your webstore.\n" +
                " Usage: /ds connect <application_api_location> <secret_key>";
    }

    @Override
    public CommandType commandType() {
        return CommandType.CONSOLE;
    }
}
