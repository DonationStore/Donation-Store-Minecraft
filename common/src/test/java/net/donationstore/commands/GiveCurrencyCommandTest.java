package net.donationstore.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.donationstore.enums.CommandType;
import net.donationstore.exception.ClientException;
import net.donationstore.http.WebstoreHTTPClient;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.models.response.GatewayResponse;
import net.donationstore.models.response.GiveCurrencyResponse;
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

public class GiveCurrencyCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public WebstoreHTTPClient webstoreHTTPClient;

    private ObjectMapper objectMapper;

    private GiveCurrencyCommand giveCurrencyCommand;

    private GiveCurrencyResponse giveCurrencyResponse;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        giveCurrencyCommand = new GiveCurrencyCommand();
        giveCurrencyResponse = objectMapper.readValue("{\"message\": \"10 EUR given to 28408e37-5b7d-4c6d-b723-b7a845418dcd\"}", GiveCurrencyResponse.class);
        ReflectionTestUtils.setField(giveCurrencyCommand, "webstoreHTTPClient", webstoreHTTPClient);
    }

    @Test
    public void gettersSettersTest() {
        // then
        assertEquals("currency", giveCurrencyCommand.getSupportedCommand());
        assertEquals("This command is used to award a player with in-game virtual currency.\n" +
                " Usage: /ds currency <player_to_give> <currency_code> <amount>", giveCurrencyCommand.helpInfo());
        assertEquals(CommandType.PLAYER, giveCurrencyCommand.commandType());
    }

    @Test
    public void runCommandTest() throws Exception {
        // given
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setBody(giveCurrencyResponse);
        given(giveCurrencyCommand.getWebstoreHTTPClient().sendRequest(any(GatewayRequest.class), any(Class.class))).willReturn(gatewayResponse);

        // when
        ArrayList<String> logs = giveCurrencyCommand.runCommand();

        // then
        assertEquals("[10 EUR given to 28408e37-5b7d-4c6d-b723-b7a845418dcd]", logs.toString());
    }

    @Test
    public void runCommandWhenClientExceptionTest() throws Exception {
        // then
        thrown.expect(ClientException.class);
        thrown.expectMessage("Example exception");

        // given
        given(giveCurrencyCommand.getWebstoreHTTPClient().sendRequest(any(GatewayRequest.class), any(Class.class))).willThrow(new ClientException("Example exception"));

        // when
        ArrayList<String> logs = giveCurrencyCommand.runCommand();
    }
}
