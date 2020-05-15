package net.donationstore.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.donationstore.exception.ClientException;
import net.donationstore.http.WebstoreHTTPClient;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.models.request.UpdateCommandExecutedRequest;
import net.donationstore.models.response.GatewayResponse;
import net.donationstore.models.response.QueueResponse;
import net.donationstore.models.response.UpdateCommandExecutedResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

public class CommandManagerTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private WebstoreHTTPClient webstoreHTTPClient;

    private ObjectMapper objectMapper;
    private QueueResponse queueResponse;
    private CommandManager commandManager;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        commandManager = new CommandManager("example", "https://example.com");
        ReflectionTestUtils.setField(commandManager, "webstoreHTTPClient", webstoreHTTPClient);
        queueResponse = objectMapper.readValue("{\"payments\": [{\"commands\": [{\"command\": \"give {username} 1\", \"id\": 1, \"uuid\": \"5b05c7cc-3118-41c7-8993-ef9f3ba5fb63\" }, {\"command\": \"give {uuid} 2\", \"id\": 2, \"uuid\": \"5b05c7cc-3118-41c7-8993-ef9f3ba5fb63\" }, {\"command\": \"give {item_type} 2\", \"id\": 3, \"uuid\": \"5b05c7cc-3118-41c7-8993-ef9f3ba5fb63\" }, {\"command\": \"give {transactionId} 2\", \"id\": 4, \"uuid\": \"5b05c7cc-3118-41c7-8993-ef9f3ba5fb63\" }], \"meta\": {\"payment_id\": 1, \"user\": \"MCxJB\", \"transaction_id\": \"SAKDN89H3198H\", \"uuid\": \"28408e37-5b7d-4c6d-b723-b7a845418dcd\"}, \"variables\": [{\"identifier\": \"item_type\", \"choice\": \"diamond_sword\"}]}]}", QueueResponse.class);
    }

    @Test
    public void getCommandTest_whenUsernameTransactionIdUUIDProvided() throws Exception {
        // given
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setBody(queueResponse);
        given(commandManager.getWebstoreHTTPClient().sendRequest(any(GatewayRequest.class), any(Class.class))).willReturn(gatewayResponse);

        // when
        QueueResponse queueResponse = commandManager.getCommands();

        // then
        assertEquals("1", queueResponse.payments.get(0).meta.paymentId);
        assertEquals("MCxJB", queueResponse.payments.get(0).meta.user);
        assertEquals("SAKDN89H3198H", queueResponse.payments.get(0).meta.transactionId);
        assertEquals("28408e37-5b7d-4c6d-b723-b7a845418dcd", queueResponse.payments.get(0).meta.uuid);
        assertEquals("give MCxJB 1", queueResponse.payments.get(0).commands.get(0).command);
        assertEquals("give 5b05c7cc-3118-41c7-8993-ef9f3ba5fb63 2", queueResponse.payments.get(0).commands.get(1).command);
        assertEquals("give diamond_sword 2", queueResponse.payments.get(0).commands.get(2).command);
        assertEquals("give SAKDN89H3198H 2", queueResponse.payments.get(0).commands.get(3).command);
        assertEquals("item_type", queueResponse.payments.get(0).variables.get(0).identifier);
        assertEquals("diamond_sword", queueResponse.payments.get(0).variables.get(0).choice);
    }

    @Test
    public void updateCommandsToExecutedTest() throws Exception {
        // given
        ArrayList<Integer> commandsToExecute = new ArrayList<>();
        commandsToExecute.add(1);
        commandsToExecute.add(2);

        UpdateCommandExecutedRequest updateCommandExecutedRequest = new UpdateCommandExecutedRequest();
        updateCommandExecutedRequest.setCommands(commandsToExecute);

        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setBody(objectMapper.readValue("{\"message\": \"Executed commands apart from the following:\", \"commands_not_found\": [2]}", UpdateCommandExecutedResponse.class));
        given(commandManager.getWebstoreHTTPClient().sendRequest(any(GatewayRequest.class), any(Class.class))).willReturn(gatewayResponse);

        // when
        UpdateCommandExecutedResponse updateCommandExecutedResponse = commandManager.updateCommandsToExecuted(updateCommandExecutedRequest);

        // then
        assertTrue(updateCommandExecutedResponse.getCommandsNotFound().isPresent());
        assertEquals(Integer.valueOf(2), updateCommandExecutedResponse.getCommandsNotFound().get().get(0));
        assertEquals("Executed commands apart from the following:", updateCommandExecutedResponse.getMessage());
    }
}
