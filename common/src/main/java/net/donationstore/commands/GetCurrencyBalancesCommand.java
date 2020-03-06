package net.donationstore.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.donationstore.exception.InvalidCommandUseException;
import net.donationstore.exception.WebstoreAPIException;
import net.donationstore.util.FormUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetCurrencyBalancesCommand extends AbstractCommand {

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

        setSecretKey(args[1]);
        setWebstoreAPILocation(args[2]);
        setUsername(args[3]);
        return this;
    }

    @Override
    public ArrayList<String> runCommand() throws Exception {

        Map<String, String> data = new HashMap<>();
        data.put("username", getUsername());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/get-currency-balances", getWebstoreAPILocation())))
                .header("secret-key", getSecretKey())
                .POST(FormUtil.ofFormData(data))
                .build();

        try {
            HttpResponse<String> response = getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = new JsonParser().parse(response.body()).getAsJsonObject();

                getLogs().add(jsonResponse.get("message").getAsString());

            } else {
                getLogs().add("Invalid webstore API response:");
                getLogs().add(response.body());
                throw new WebstoreAPIException(getLogs());
            }
        } catch (IOException exception) {
            throw new IOException("IOException when contacting the webstore API when running the get currency balances command.");
        } catch (InterruptedException exception) {
            throw new InterruptedException("InterruptedException when contacting the webstore API when running the get currency balances command.");
        }

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
