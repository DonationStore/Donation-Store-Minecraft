package net.donationstore.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.donationstore.enums.CommandType;
import net.donationstore.exception.ClientException;
import net.donationstore.http.WebstoreHTTPClient;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.models.response.GatewayResponse;
import net.donationstore.models.response.InformationResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

public class ConnectCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public WebstoreHTTPClient webstoreHTTPClient;

    private ObjectMapper objectMapper;

    private ConnectCommand connectCommand;

    private InformationResponse informationResponse;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        connectCommand = new ConnectCommand();
        informationResponse = objectMapper.readValue("{\"webstore\": {\"currency\": \"EUR\", \"id\": 1, \"name\": \"Example Store\"}, \"server\": {\"ip\": \"127.0.0.1\", \"id\": 1, \"name\": \"Hello World\"}}", InformationResponse.class);
        ReflectionTestUtils.setField(connectCommand, "webstoreHTTPClient", webstoreHTTPClient);
    }

    @Test
    public void gettersSettersTest() {
        // then
        assertEquals("connect", connectCommand.getSupportedCommand());
        assertEquals("This command is used to connect the Donation Store plugin to your webstore.\n" +
                " Usage: /ds connect <application_api_location> <secret_key>", connectCommand.helpInfo());
        assertEquals(CommandType.CONSOLE, connectCommand.commandType());
    }

    @Test
    public void runCommandTest() throws Exception {
        // given
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setBody(informationResponse);
        given(connectCommand.getWebstoreHTTPClient().sendRequest(any(GatewayRequest.class), any(Class.class))).willReturn(gatewayResponse);

        // when
        ArrayList<String> logs = connectCommand.runCommand();

        // then
        assertEquals("[Connected to webstore Example Store, Donation Store will now start retrieving commands occasionally]", logs.toString());
    }

    @Test
    public void runCommandWhenClientExceptionTest() throws Exception {
        // then
        thrown.expect(ClientException.class);
        thrown.expectMessage("Example exception");

        // given
        given(connectCommand.getWebstoreHTTPClient().sendRequest(any(GatewayRequest.class), any(Class.class))).willThrow(new ClientException("Example exception"));

        // when
        ArrayList<String> logs = connectCommand.runCommand();
    }
}
