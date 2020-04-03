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

                if (command.command.contains("{username}")) {
                    command.command = command.command.replace(USERNAME_IDENTIFIER, payment.meta.user);
                }

                if (command.command.contains("transactionId")) {
                    command.command = command.command.replace(TRANSACTION_ID_IDENTIFIER, payment.meta.transactionId);
                }

                if (command.command.contains("{uuid}")) {
                    command.command = command.command.replace(UUID_IDENTIFIER, payment.meta.uuid);
                }
            }
        }

        return queueResponse;
    }

    public WebstoreHTTPClient getWebstoreHTTPClient() {
        return webstoreHTTPClient;
    }
}
