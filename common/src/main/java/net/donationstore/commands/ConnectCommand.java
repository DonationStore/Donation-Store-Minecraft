package net.donationstore.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.donationstore.exception.InvalidCommandUseException;
import net.donationstore.exception.WebstoreAPIException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ConnectCommand extends AbstractCommand {

    private String secretKey;

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

        setSecretKey(args[1]);
        setWebstoreAPILocation(args[2]);
        return this;
    }

    @Override
    public ArrayList<String> runCommand() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/information", getWebstoreAPILocation())))
                .header("secret-key", getSecretKey())
                .build();
        try {
            HttpResponse<String> response = getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = new JsonParser().parse(response.body()).getAsJsonObject();

                JsonObject storeInformation = jsonResponse.getAsJsonObject("webstore");

                JsonObject serverInformation = jsonResponse.getAsJsonObject("server");

                getLogs().add(String.format("Connected to Webstore: @s", storeInformation.get("name").getAsString()));
                getLogs().add("Commands will now be executed when packages are purchased.");
            } else {
                getLogs().add("Invalid webstore API response:");
                getLogs().add(response.body());
                throw new WebstoreAPIException(getLogs());
            }
        } catch (IOException exception) {
            throw new IOException("IOException when contacting the webstore API when running the connect command", exception);
        } catch (InterruptedException exception) {
            throw new InterruptedException("InterruptedException when contacting the webstore API when running the connect command");
        }

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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
