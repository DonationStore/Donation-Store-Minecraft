package net.donationstore.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.donationstore.enums.CommandType;
import net.donationstore.exception.ClientException;
import net.donationstore.http.WebstoreHTTPClient;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.models.response.CurrencyCodeResponse;
import net.donationstore.models.response.GatewayResponse;
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

public class GetCurrencyCodeCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public WebstoreHTTPClient webstoreHTTPClient;

    private ObjectMapper objectMapper;

    private GetCurrencyCodeCommand getCurrencyCodeCommand;

    private CurrencyCodeResponse currencyCodeResponse;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        getCurrencyCodeCommand = new GetCurrencyCodeCommand();
        currencyCodeResponse = objectMapper.readValue("{\"code\": \"D3CRWAZ47A\", \"uuid\": \"28408e37-5b7d-4c6d-b723-b7a845418dcd\"}", CurrencyCodeResponse.class);
        ReflectionTestUtils.setField(getCurrencyCodeCommand, "webstoreHTTPClient", webstoreHTTPClient);
    }

    @Test
    public void gettersSettersTest() {
        // then
        assertEquals("code", getCurrencyCodeCommand.getSupportedCommand());
        assertEquals("This command is used to generate a Virtual Currency Claim code used on a webstore to pay with Virtual Currencies.", getCurrencyCodeCommand.helpInfo());
        assertEquals(CommandType.PLAYER, getCurrencyCodeCommand.commandType());
    }

    @Test
    public void runCommandTest() throws Exception {
        // given
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setBody(currencyCodeResponse);
        given(getCurrencyCodeCommand.getWebstoreHTTPClient().sendRequest(any(GatewayRequest.class), any(Class.class))).willReturn(gatewayResponse);

        // when
        ArrayList<String> logs = getCurrencyCodeCommand.runCommand();

        // then
        assertEquals("[Currency Code: D3CRWAZ47A]", logs.toString());
    }

    @Test
    public void runCommandWhenClientExceptionTest() throws Exception {
        // given
        thrown.expect(ClientException.class);
        thrown.expectMessage("Example exception");

        // given
        given(getCurrencyCodeCommand.getWebstoreHTTPClient().sendRequest(any(GatewayRequest.class), any(Class.class))).willThrow(new ClientException("Example exception"));

        // when
        ArrayList<String> logs = getCurrencyCodeCommand.runCommand();
    }
}
