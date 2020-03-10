package net.donationstore.commands;

import net.donationstore.models.request.CurrencyCodeRequest;
import net.donationstore.models.response.CurrencyCodeResponse;
import net.donationstore.models.response.GatewayResponse;
import net.donationstore.enums.CommandType;
import net.donationstore.enums.HttpMethod;
import net.donationstore.exception.InvalidCommandUseException;

import java.util.ArrayList;

public class GetCurrencyCodeCommand extends AbstractApiCommand {
    
    private CurrencyCodeRequest currencyCodeRequest;

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

        currencyCodeRequest = new CurrencyCodeRequest();
        currencyCodeRequest.setUuid(args[2]);
        return this;
    }

    @Override
    public ArrayList<String> runCommand() throws Exception {

        GatewayResponse gatewayResponse = getWebstoreHTTPClient().sendRequest(
                buildDefaultRequest("currency/code/generate", HttpMethod.POST, currencyCodeRequest),
                CurrencyCodeResponse.class);

        CurrencyCodeResponse currencyCodeResponse = (CurrencyCodeResponse) gatewayResponse.getBody();

        addLog(String.format("Currency Code: %s", currencyCodeResponse.getCode()));

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
}
