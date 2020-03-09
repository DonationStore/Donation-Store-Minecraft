package net.donationstore.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.donationstore.commands.*;
import net.donationstore.enums.HttpMethod;
import net.donationstore.exception.ClientException;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.models.response.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class WebstoreHTTPClientTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Spy
    public WebstoreHTTPClient webstoreHTTPClient;

    private CommandFactory commandFactory;

    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {

        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        objectMapper = new ObjectMapper();

        webstoreHTTPClient.setSecretKey("secretKey")
                .setWebstoreAPILocation("https://example.com");

        ReflectionTestUtils.setField(webstoreHTTPClient, "httpClient", httpClient);

        ReflectionTestUtils.setField(webstoreHTTPClient, "objectMapper", objectMapper);
    }

    @Test
    public void gettersSettersTest() {
        // then
        assertEquals("secretKey", webstoreHTTPClient.getSecretKey());
        assertEquals("https://example.com", webstoreHTTPClient.getWebstoreAPILocation());
    }

    @Test
    public void connectCommandTest() throws Exception {
        // given
        doReturn("{\"webstore\": {\"currency\": \"EUR\", \"id\": 1, \"name\": \"Example Store\"}, \"server\": {\"ip\": \"127.0.0.1\", \"id\": 1, \"name\": \"Hello World\"}}").when(webstoreHTTPClient).sendHttpRequest(any(HttpClient.class), any(HttpRequest.class));
        ConnectCommand connect = new ConnectCommand();
        connect.validate(new String[]{"connect", "secretKey", "https://example.com"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("information", HttpMethod.GET), InformationResponse.class);
        InformationResponse informationResponse = (InformationResponse) gatewayResponse.getBody();

        // then
        assertEquals(informationResponse.webstore.get("currency"), "EUR");
        assertEquals(informationResponse.webstore.get("id"), 1);
        assertEquals(informationResponse.webstore.get("name"), "Example Store");
        assertEquals(informationResponse.server.get("ip"), "127.0.0.1");
        assertEquals(informationResponse.server.get("name"), "Hello World");
    }

    @Test
    public void getCurrencyBalancesTest() throws Exception {
        // given
        doReturn("{\"username\": \"MCxJB\", \"uuid\": \"28408e37-5b7d-4c6d-b723-b7a845418dcd\", \"balances\": {\"EUR\": \"1.00\"}}").when(webstoreHTTPClient).sendHttpRequest(any(HttpClient.class), any(HttpRequest.class));
        GetCurrencyBalancesCommand getCurrencyBalancesCommand = new GetCurrencyBalancesCommand();
        getCurrencyBalancesCommand.validate(new String[]{"balance", "secretKey", "https://example.com", "28408e37-5b7d-4c6d-b723-b7a845418dcd"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("currency/balances", HttpMethod.POST), CurrencyBalanceResponse.class);
        CurrencyBalanceResponse currencyBalanceResponse = (CurrencyBalanceResponse) gatewayResponse.getBody();

        // then
        assertEquals("MCxJB", currencyBalanceResponse.username);
        assertEquals(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"), currencyBalanceResponse.uuid);
        assertEquals("1.00", currencyBalanceResponse.balances.get("EUR"));
    }

    @Test
    public void getCurrencyCodeTest() throws Exception {
        // given
        doReturn("{\"code\": \"D3CRWAZ47A\", \"uuid\": \"28408e37-5b7d-4c6d-b723-b7a845418dcd\"}").when(webstoreHTTPClient).sendHttpRequest(any(HttpClient.class), any(HttpRequest.class));
        GetCurrencyCodeCommand getCurrencyCodeCommand = new GetCurrencyCodeCommand();
        getCurrencyCodeCommand.validate(new String[]{"code", "secretKey", "https://example.com", "28408e37-5b7d-4c6d-b723-b7a845418dcd"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("currency/code/generate", HttpMethod.POST), CurrencyCodeResponse.class);
        CurrencyCodeResponse currencyCodeResponse = (CurrencyCodeResponse) gatewayResponse.getBody();

        // then
        assertEquals("D3CRWAZ47A", currencyCodeResponse.code);
        assertEquals(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"), currencyCodeResponse.uuid);
    }

    @Test
    public void giveCurrencyCodeTest() throws Exception {
        // given
        doReturn("{\"message\": \"10 EUR given to 28408e37-5b7d-4c6d-b723-b7a845418dcd\"}").when(webstoreHTTPClient).sendHttpRequest(any(HttpClient.class), any(HttpRequest.class));
        GiveCurrencyCommand giveCurrencyCommand = new GiveCurrencyCommand();
        giveCurrencyCommand.validate(new String[]{"give", "secretKey", "https://example.com", "28408e37-5b7d-4c6d-b723-b7a845418dcd", "EUR", "10"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("currency/give", HttpMethod.POST), GiveCurrencyResponse.class);
        GiveCurrencyResponse giveCurrencyResponseDTO = (GiveCurrencyResponse) gatewayResponse.getBody();

        // then
        assertEquals("10 EUR given to 28408e37-5b7d-4c6d-b723-b7a845418dcd", giveCurrencyResponseDTO.message);
    }

    @Test
    public void clientExceptionTest() throws Exception {
        // then
        thrown.expect(ClientException.class);
        doThrow(IOException.class).when(webstoreHTTPClient).sendHttpRequest(any(HttpClient.class), any(HttpRequest.class));
        ConnectCommand connect = new ConnectCommand();
        connect.validate(new String[]{"connect", "secretKey", "https://example.com"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("information", HttpMethod.GET), InformationResponse.class);
        InformationResponse informationResponse = (InformationResponse) gatewayResponse.getBody();

    }

    private GatewayRequest buildRequest(String resourceUrl, HttpMethod method) throws URISyntaxException {
        GatewayRequest request = new GatewayRequest();
        request.setUri("https://example.com" + resourceUrl);
        request.setMethod(method);
        request.setHeaders(getDefaultHeaders());
        return request;
    }

    public Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("secret-key", "secretKey");
        return headers;
    }
}
