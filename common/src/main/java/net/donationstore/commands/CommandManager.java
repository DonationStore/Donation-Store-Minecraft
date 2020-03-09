package net.donationstore.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.donationstore.models.CommandExectionPayloadDTO;
import net.donationstore.models.QueueDTO;
import net.donationstore.exception.WebstoreAPIException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

public class CommandManager {

    private QueueDTO queueDTO;
    private HttpClient httpClient;
    private ArrayList<String> logs;
    private ObjectMapper objectMapper;
    private CommandFactory commandFactory;

    public CommandManager() {

        commandFactory = new CommandFactory();

        logs = new ArrayList<>();
        queueDTO = new QueueDTO();
        objectMapper = new ObjectMapper();

        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    // Execute command method
    public void executeCommand(String[] args) throws Exception {
        logs = commandFactory.getCommand(args).runCommand();
    }

/*    public boolean updateCommandsToExecuted(String secretKey, String webstoreAPILocation, ArrayList<String> commands) throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("commands", commands.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/commands/execute", webstoreAPILocation)))
                .header("secret-key", secretKey)
                .POST(FormUtil.ofFormData(data))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Do something
                return true;
            }
        } catch(IOException exception) {
            throw new IOException("IOException when contacting your webstore to give list of executed commands.");
        } catch(InterruptedException exception) {
            throw new InterruptedException("IOException when contacting your webstore to give list of executed commands.");
        }

        return true;
    }*/

    public QueueDTO getCommands(String secretKey, String webstoreAPILocation) throws Exception {

        ArrayList<String> logs = new ArrayList<>();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/queue", webstoreAPILocation)))
                .header("secret-key", secretKey)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonArray jsonResponse = new JsonParser().parse(response.body()).getAsJsonArray();

                for(JsonElement payment: jsonResponse) {
                    CommandExectionPayloadDTO commandExectionPayloadDTO = objectMapper.readValue(payment.getAsString(), CommandExectionPayloadDTO.class);

                    // Here go through all of the commands and if they contain either username or the identifier then replace it
                    for(Map.Entry<String, String> command: commandExectionPayloadDTO.commands.entrySet()) {
                        // Key (id), value == command
                        for(Map.Entry<String, String> variable: commandExectionPayloadDTO.variables.entrySet()) {
                            // Identifier = key
                            // Choice = value
                            String variableIdentifierWithBraces = String.format("{%s}", variable.getValue());
                            if(command.getValue().contains(variableIdentifierWithBraces)) {
                                commandExectionPayloadDTO.commands.replace(variableIdentifierWithBraces, command.getValue().replace(variableIdentifierWithBraces, variable.getValue()));
                            }
                        }

                        // Other variables can be added here
                        if(command.getValue().contains("{username}")) {
                            commandExectionPayloadDTO.commands.replace(command.getKey(), command.getValue().replace("{username}", commandExectionPayloadDTO.meta.get("user")));
                        }

                        if(command.getValue().contains("{transactionId}")) {
                            commandExectionPayloadDTO.commands.replace(command.getKey(), command.getValue().replace("{transactionId}", commandExectionPayloadDTO.meta.get("transaction_id")));
                        }

                        if(command.getValue().contains("{uuid}")) {
                            commandExectionPayloadDTO.commands.replace(command.getKey(), command.getValue().replace("{uuid}", commandExectionPayloadDTO.meta.get("uuid")));
                        }
                    }
                    queueDTO.commandExectionPayloadDTO.add(commandExectionPayloadDTO);
                }

                return queueDTO;
            } else {
                logs.add("Invalid webstore API response when getting command queue: ");
                logs.add(response.body());
                throw new WebstoreAPIException(logs);
            }
        } catch(IOException exception) {
            throw new IOException("IOException when contacting your webstore to retrieve the command queue.");
        } catch(InterruptedException exception) {
            throw new InterruptedException("IOException when contacting your webstore to retrieve the command queue.");
        }
    }
}
