package net.donationstore.commands;

import net.donationstore.models.response.GatewayResponse;
import net.donationstore.models.response.InformationResponse;
import net.donationstore.enums.CommandType;
import net.donationstore.enums.HttpMethod;
import net.donationstore.exception.InvalidCommandUseException;

import java.util.ArrayList;

public class ConnectCommand extends AbstractApiCommand {

    @Override
    public String getSupportedCommand() {
        return "connect";
    }

    @Override
    public Command validate(String[] args) {
        if (args.length != 2) {
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

        GatewayResponse gatewayResponse = getWebstoreHTTPClient().sendRequest(
                buildDefaultRequest("information", HttpMethod.GET, null),
                InformationResponse.class);

        InformationResponse informationResponse = (InformationResponse) gatewayResponse.getBody();

        addLog(String.format("Connected to webstore %s", informationResponse.getWebstore().get("name")));
        addLog("Donation Store will now start retrieving commands occasionally");

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
