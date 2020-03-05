package net.donationstore.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.donationstore.exception.InvalidCommandUseException;
import net.donationstore.exception.WebstoreAPIException;
import net.donationstore.util.FormUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetCurrencyBalancesCommand implements Command {

    @JsonProperty("username")
    private String username;

    @JsonIgnore
    private String secretKey;

    @JsonIgnore
    private String webstoreAPILocation;

    @JsonIgnore
    private HttpClient httpClient;

    @JsonIgnore
    private ArrayList<String> logs;

    public GetCurrencyBalancesCommand() {
        logs = new ArrayList<>();
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    @Override
    public ArrayList<String> runCommand(String[] args) throws Exception {

        // The API location and secret key are hard coded in this when it's sent
        // so if its less than that then it means they are missing their username
        if (args.length != 3) {
            logs.add("Invalid usage of command. Help Info: ");
            logs.add(this.helpInfo());
            throw new InvalidCommandUseException(logs);
        } else {
            String webstoreAPILocation = args[0];
            String username = args[1];
            String secretKey = args[2];
            Map<String, String> data = new HashMap<>();
            data.put("username", username);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("%s/get-currency-balances", webstoreAPILocation)))
                    .header("secret-key", secretKey)
                    .POST(FormUtil.ofFormData(data))
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonObject jsonResponse = new JsonParser().parse(response.body()).getAsJsonObject();

                    logs.add(jsonResponse.get("message").getAsString());

                } else {
                    logs.add("Invalid webstore API response:");
                    logs.add(response.body());
                    throw new WebstoreAPIException(logs);
                }
            } catch (IOException exception) {
                throw new IOException("IOException when contacting the webstore API when running the get currency balances command.");
            } catch (InterruptedException exception) {
                throw new InterruptedException("InterruptedException when contacting the webstore API when running the get currency balances command.");
            }
        }

        return logs;
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

    public String getSecretKey() {
        return secretKey;
    }

    public GetCurrencyBalancesCommand setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getWebstoreAPILocation() {
        return webstoreAPILocation;
    }

    public GetCurrencyBalancesCommand setWebstoreAPILocation(String webstoreAPILocation) {
        this.webstoreAPILocation = webstoreAPILocation;
        return this;
    }
}
