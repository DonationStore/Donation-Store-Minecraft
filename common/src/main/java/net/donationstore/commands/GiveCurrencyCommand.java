package net.donationstore.commands;

import net.donationstore.models.request.GiveCurrencyRequest;
import net.donationstore.models.response.GatewayResponse;
import net.donationstore.models.response.GiveCurrencyResponse;
import net.donationstore.enums.CommandType;
import net.donationstore.enums.HttpMethod;
import net.donationstore.exception.InvalidCommandUseException;

import java.util.ArrayList;

public class GiveCurrencyCommand extends AbstractApiCommand {

    public GiveCurrencyCommand() {
        setPermission("donationstore.currency");
    }

    private GiveCurrencyRequest giveCurrencyRequest;

    @Override
    public String getSupportedCommand() {
        return "currency";
    }

    @Override
    public Command validate(String[] args) {
        if (args.length != 6) {
            getLogs().add(getInvalidCommandMessage());
            getLogs().add(helpInfo());
            throw new InvalidCommandUseException(returnAndClearLogs());
        }

        getWebstoreHTTPClient().setSecretKey(args[0])
                .setWebstoreAPILocation(args[1]);

        giveCurrencyRequest = new GiveCurrencyRequest();
        giveCurrencyRequest.setUsername(args[2])
                .setCurrencyCode(args[3])
                .setAmount(args[4]);

        return this;
    }

    @Override
    public ArrayList<String> runCommand() throws Exception {

        GatewayResponse gatewayResponse = getWebstoreHTTPClient().sendRequest(
                getWebstoreHTTPClient().buildDefaultRequest("currency/give", HttpMethod.POST, giveCurrencyRequest),
                GiveCurrencyResponse.class);

        GiveCurrencyResponse giveCurrencyResponse = (GiveCurrencyResponse) gatewayResponse.getBody();

        addLog(giveCurrencyResponse.getMessage());

        return returnAndClearLogs();
    }

    @Override
    public String helpInfo() {
        return "This command is used to award a player with in-game virtual currency.\n" +
                " Usage: /ds currency <player_to_give> <currency_code> <amount>";
    }

    @Override
    public CommandType commandType() {
        return CommandType.PLAYER;
    }
}
