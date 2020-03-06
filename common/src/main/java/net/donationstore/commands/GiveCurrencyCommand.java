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

public class GiveCurrencyCommand extends AbstractCommand {

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

        setSecretKey(args[1]);
        setWebstoreAPILocation(args[2]);
        setCurrencyCode(args[3]);
        setAmount(args[4]);
        setUsername(args[5]);

        return this;
    }

    @Override
    public ArrayList<String> runCommand() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("currency_code", currencyCode);
        data.put("amount", amount);

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
            throw new IOException("IOException when contacting the webstore API when running the give currency command.");
        } catch (InterruptedException exception) {
            throw new InterruptedException("InterruptedException when contacting the webstore API when running the give currency command.");
        }

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
