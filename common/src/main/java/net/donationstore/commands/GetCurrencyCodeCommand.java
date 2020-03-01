package net.donationstore.commands;

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

public class GetCurrencyCodeCommand implements Command {

    private HttpClient httpClient;
    private ArrayList<String> logs;

    public GetCurrencyCodeCommand() {
        logs = new ArrayList<>();
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    @Override
    public ArrayList<String> runCommand(String[] args) throws Exception {

        if (args.length != 3) {
            logs.add("Invalid usage of command. Help Info: ");
            logs.add(this.helpInfo());
            throw new InvalidCommandUseException(logs);
        } else {
            String webstoreAPILocation = args[0];
            String secretKey = args[1];
            String username = args[2];
            Map<String, String> data = new HashMap<>();
            data.put("username", username);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("%s/generate-currency-code", webstoreAPILocation)))
                    .header("secret-key", secretKey)
                    .POST(FormUtil.ofFormData(data))
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonObject jsonResponse = new JsonParser().parse(response.body()).getAsJsonObject();

                    String code = jsonResponse.get("code").getAsString();

                    logs.add(String.format("Virtual Currency Claim Code: %s", code));
                    logs.add("This will expire in 5 minutes");
                } else {
                    logs.add("Invalid webstore API response:");
                    logs.add(response.body());
                    throw new WebstoreAPIException(logs);
                }
            } catch (IOException exception) {
                throw new IOException("IOException when contacting the webstore API when running the get currency code command.");
            } catch (InterruptedException exception) {
                throw new InterruptedException("InterruptedException when contacting the webstore API when running the get currency code command.");
            }
        }

        return logs;
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
