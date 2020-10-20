package net.donationstore.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.donationstore.enums.HttpMethod;
import net.donationstore.http.WebstoreHTTPClient;
import net.donationstore.models.Variable;
import net.donationstore.models.request.UpdateCommandExecutedRequest;
import net.donationstore.models.response.GatewayResponse;
import net.donationstore.models.response.PaymentsResponse;
import net.donationstore.models.response.QueueResponse;
import net.donationstore.models.Command;
import net.donationstore.models.response.UpdateCommandExecutedResponse;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager {

    private static String UUID_IDENTIFIER = "{uuid}";
    private static String USERNAME_IDENTIFIER = "{username}";
    private static String TRANSACTION_ID_IDENTIFIER = "{transactionId}";
    private static String SERVER_IDENTIFIER = "{server}";
    private static String AMOUNT_IDENTIFIER = "{amount}";
    private static String PACKAGE_ID_IDENTIFIER = "{packageId}";
    private static String PACKAGE_PRICE_IDENTIFIER = "{packagePrice}";
    private static String PACKAGE_NAME_IDENTIFIER = "{packageName}";

    private ArrayList<String> logs;
    private ObjectMapper objectMapper;
    private CommandFactory commandFactory;

    private WebstoreHTTPClient webstoreHTTPClient;

    public CommandManager(String secretKey, String webstoreAPILocation) {

        commandFactory = new CommandFactory();

        logs = new ArrayList<>();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        webstoreHTTPClient = new WebstoreHTTPClient();
        webstoreHTTPClient.setSecretKey(secretKey)
                .setWebstoreAPILocation(webstoreAPILocation);
    }

    public UpdateCommandExecutedResponse updateCommandsToExecuted(UpdateCommandExecutedRequest updateCommandExecutedRequest) throws Exception {
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(
                webstoreHTTPClient.buildDefaultRequest("commands/execute", HttpMethod.POST, updateCommandExecutedRequest),
                UpdateCommandExecutedResponse.class
        );

        return (UpdateCommandExecutedResponse) gatewayResponse.getBody();
    }

    public QueueResponse getCommands() throws Exception {

        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(
                webstoreHTTPClient.buildDefaultRequest("queue", HttpMethod.GET, null),
                QueueResponse.class
        );

        QueueResponse queueResponse = (QueueResponse) gatewayResponse.getBody();

        for (PaymentsResponse payment : queueResponse.payments) {
            for (Command command : payment.commands) {
                for (Variable variable : payment.variables) {
                    String variableIdentifierWithBraces = String.format("{%s}", variable.identifier);

                    if (command.command.contains(variableIdentifierWithBraces)) {
                        command.command = command.command.replace(variableIdentifierWithBraces, variable.choice);
                    }
                }

                if (command.command.contains(USERNAME_IDENTIFIER)) {
                    command.command = command.command.replace(USERNAME_IDENTIFIER, command.username);
                }

                if (command.command.contains(TRANSACTION_ID_IDENTIFIER)) {
                    command.command = command.command.replace(TRANSACTION_ID_IDENTIFIER, payment.meta.transactionId);
                }

                if (command.command.contains(UUID_IDENTIFIER)) {
                    command.command = command.command.replace(UUID_IDENTIFIER, command.uuid);
                }

                if (command.command.contains(SERVER_IDENTIFIER)) {
                    command.command = command.command.replace(SERVER_IDENTIFIER, command.server);
                }

                if (command.command.contains(AMOUNT_IDENTIFIER)) {
                    command.command = command.command.replace(AMOUNT_IDENTIFIER, command.amount);
                }

                if (command.command.contains(PACKAGE_ID_IDENTIFIER)) {
                    command.command = command.command.replace(PACKAGE_ID_IDENTIFIER, Integer.toString(command.packageResponse.id));
                }

                if (command.command.contains(PACKAGE_PRICE_IDENTIFIER)) {
                    command.command = command.command.replace(PACKAGE_PRICE_IDENTIFIER, command.packageResponse.price);
                }

                if (command.command.contains(PACKAGE_NAME_IDENTIFIER)) {
                    command.command = command.command.replace(PACKAGE_NAME_IDENTIFIER, command.packageResponse.name);
                }
            }
        }

        return queueResponse;
    }

    public WebstoreHTTPClient getWebstoreHTTPClient() {
        return webstoreHTTPClient;
    }
}
