package net.donationstore.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.donationstore.exception.ClientException;
import net.donationstore.http.WebstoreHTTPClient;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.models.response.CurrencyBalanceResponse;
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
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

public class GetCurrencyBalancesCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public WebstoreHTTPClient webstoreHTTPClient;

    private ObjectMapper objectMapper;

    private CurrencyBalanceResponse currencyBalanceResponse;

    private GetCurrencyBalancesCommand getCurrencyBalancesCommand;

    @Before
    public void setup() throws Exception {
        getCurrencyBalancesCommand  = new GetCurrencyBalancesCommand();
        objectMapper = new ObjectMapper();
        currencyBalanceResponse = objectMapper.readValue("{\"username\": \"MCxJB\", \"uuid\": \"28408e37-5b7d-4c6d-b723-b7a845418dcd\", \"balances\": {\"EUR\": \"1.00\"}}", CurrencyBalanceResponse.class);
        ReflectionTestUtils.setField(getCurrencyBalancesCommand, "webstoreHTTPClient", webstoreHTTPClient);
    }

    @Test
    public void gettersSettersTest() {
        // then
        assertEquals("MCxJB", currencyBalanceResponse.getUsername());
        assertEquals(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"), currencyBalanceResponse.getUuid());
        assertEquals("1.00", currencyBalanceResponse.getBalances().get("EUR"));
    }

    @Test
    public void runCommandTest() throws Exception {
        // given
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setBody(currencyBalanceResponse);
        given(getCurrencyBalancesCommand.getWebstoreHTTPClient().sendRequest(any(GatewayRequest.class), any(Class.class))).willReturn(gatewayResponse);

        // when
        ArrayList<String> logs = getCurrencyBalancesCommand.runCommand();

        // then
        assertEquals("[EUR: 1.00]", logs.toString());
    }

    @Test
    public void runCommandWhenClientExceptionTest() throws Exception {
        // then
        thrown.expect(ClientException.class);
        thrown.expectMessage("Example exception");

        // given
        given(getCurrencyBalancesCommand.getWebstoreHTTPClient().sendRequest(any(GatewayRequest.class), any(Class.class))).willThrow(new ClientException("Example exception"));

        // when
        ArrayList<String> logs = getCurrencyBalancesCommand.runCommand();
    }
}
