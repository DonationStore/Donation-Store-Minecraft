package net.donationstore.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.donationstore.dto.WebstoreAPIResponseDTO;
import net.donationstore.exception.InvalidCommandUseException;

import java.util.ArrayList;


public class GetCurrencyBalancesCommand extends AbstractApiCommand {

    @JsonProperty("username")
    private String username;

    @Override
    public String getSupportedCommand() {
        return "balance";
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

        setUsername(args[2]);
        return this;
    }

    @Override
    public ArrayList<String> runCommand() throws Exception {

        WebstoreAPIResponseDTO webstoreAPIResponseDTO = getWebstoreHTTPClient().post(this, "currency/balances");

        // Do stuff with the body
        return getLogs();
    }

    @Override
    public String helpInfo() {
        return "This command is used to view your Virtual Currency balances.";
    }

    @Override
    public CommandType commandType() {
        return CommandType.PLAYER;
    }

    public String getUsername() {
        return username;
    }

    public GetCurrencyBalancesCommand setUsername(String username) {
        this.username = username;
        return this;
    }
}
