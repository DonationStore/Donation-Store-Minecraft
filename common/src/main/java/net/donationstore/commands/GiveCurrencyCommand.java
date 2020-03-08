package net.donationstore.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.donationstore.dto.WebstoreAPIResponseDTO;
import net.donationstore.exception.InvalidCommandUseException;

import java.util.ArrayList;

public class GiveCurrencyCommand extends AbstractApiCommand {

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("username")
    private String username;

    @JsonProperty("currency-code")
    private String currencyCode;

    @Override
    public String getSupportedCommand() {
        return "currency";
    }

    @Override
    public Command validate(String[] args) {
        if (args.length != 5) {
            getLogs().add(getInvalidCommandMessage());
            getLogs().add(helpInfo());
            throw new InvalidCommandUseException(getLogs());
        }

        getWebstoreHTTPClient().setSecretKey(args[0])
                .setWebstoreAPILocation(args[1]);

        setCurrencyCode(args[3]);
        setAmount(args[4]);
        setUsername(args[5]);

        return this;
    }

    @Override
    public ArrayList<String> runCommand() throws Exception {

        WebstoreAPIResponseDTO webstoreAPIResponseDTO = getWebstoreHTTPClient().post(this, "currency/give");

        // Do stuff with the body

        return getLogs();
    }

    @Override
    public String helpInfo() {
        return "This command is used to award a player with in-game virtual currency.";
    }

    @Override
    public CommandType commandType() {
        return CommandType.PLAYER;
    }

    public String getAmount() {
        return amount;
    }

    public GiveCurrencyCommand setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public GiveCurrencyCommand setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public GiveCurrencyCommand setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }
}
