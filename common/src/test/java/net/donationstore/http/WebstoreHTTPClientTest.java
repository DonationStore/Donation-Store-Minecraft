package net.donationstore.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.donationstore.commands.*;
import net.donationstore.enums.HttpMethod;
import net.donationstore.exception.ClientException;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.models.response.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    private OkHttpClient httpClient;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {

        httpClient = new OkHttpClient();

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
    public void buildDefaultRequestTest() throws Exception {
        // when
        GatewayRequest gatewayRequest = webstoreHTTPClient.buildDefaultRequest("example", HttpMethod.GET, null);

        // then
        assertEquals(URI.create("https://example.com/example"), gatewayRequest.getUri());
        assertEquals(HttpMethod.GET, gatewayRequest.getMethod());
        assertEquals("secretKey", gatewayRequest.getHeaders().get("Secret-Key"));
    }

    @Test
    public void getDefaultHeadersTest() {
        // given
        Map<String, String> headers = new HashMap<>();
        headers.put("Secret-Key", "secretKey");

        // then
        assertEquals(headers, webstoreHTTPClient.getDefaultHeaders());
    }

    @Test
    public void connectCommandTest() throws Exception {
        // given
        doReturn("{\"webstore\": {\"currency\": \"EUR\", \"id\": 1, \"name\": \"Example Store\"}, \"server\": {\"ip\": \"127.0.0.1\", \"id\": 1, \"name\": \"Hello World\"}}").when(webstoreHTTPClient).sendHttpRequest(any(OkHttpClient.class), any(Request.class));
        ConnectCommand connect = new ConnectCommand();
        connect.validate(new String[]{"secretKey", "https://example.com"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("information", HttpMethod.GET), InformationResponse.class);
        InformationResponse informationResponse = (InformationResponse) gatewayResponse.getBody();

        // then
        assertEquals(informationResponse.getWebstore().get("currency"), "EUR");
        assertEquals(informationResponse.getWebstore().get("id"), 1);
        assertEquals(informationResponse.getWebstore().get("name"), "Example Store");
        assertEquals(informationResponse.getServer().get("ip"), "127.0.0.1");
        assertEquals(informationResponse.getServer().get("name"), "Hello World");
    }

    @Test
    public void getCurrencyBalancesTest() throws Exception {
        // given
        doReturn("{\"username\": \"MCxJB\", \"uuid\": \"28408e37-5b7d-4c6d-b723-b7a845418dcd\", \"balances\": {\"EUR\": \"1.00\"}}").when(webstoreHTTPClient).sendHttpRequest(any(OkHttpClient.class), any(Request.class));
        GetCurrencyBalancesCommand getCurrencyBalancesCommand = new GetCurrencyBalancesCommand();
        getCurrencyBalancesCommand.validate(new String[]{"secretKey", "https://example.com", "28408e37-5b7d-4c6d-b723-b7a845418dcd"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("currency/balances", HttpMethod.POST), CurrencyBalanceResponse.class);
        CurrencyBalanceResponse currencyBalanceResponse = (CurrencyBalanceResponse) gatewayResponse.getBody();

        // then
        assertEquals("MCxJB", currencyBalanceResponse.getUsername());
        assertEquals(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"), currencyBalanceResponse.getUuid());
        assertEquals("1.00", currencyBalanceResponse.getBalances().get("EUR"));
    }

    @Test
    public void getCurrencyCodeTest() throws Exception {
        // given
        doReturn("{\"code\": \"D3CRWAZ47A\", \"uuid\": \"28408e37-5b7d-4c6d-b723-b7a845418dcd\"}").when(webstoreHTTPClient).sendHttpRequest(any(OkHttpClient.class), any(Request.class));
        GetCurrencyCodeCommand getCurrencyCodeCommand = new GetCurrencyCodeCommand();
        getCurrencyCodeCommand.validate(new String[]{"secretKey", "https://example.com", "28408e37-5b7d-4c6d-b723-b7a845418dcd"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("currency/code/generate", HttpMethod.POST), CurrencyCodeResponse.class);
        CurrencyCodeResponse currencyCodeResponse = (CurrencyCodeResponse) gatewayResponse.getBody();

        // then
        assertEquals("D3CRWAZ47A", currencyCodeResponse.getCode());
        assertEquals(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"), currencyCodeResponse.getUuid());
    }

    @Test
    public void giveCurrencyCodeTest() throws Exception {
        // given
        doReturn("{\"message\": \"10 EUR given to 28408e37-5b7d-4c6d-b723-b7a845418dcd\"}").when(webstoreHTTPClient).sendHttpRequest(any(OkHttpClient.class), any(Request.class));
        GiveCurrencyCommand giveCurrencyCommand = new GiveCurrencyCommand();
        giveCurrencyCommand.validate(new String[]{"secretKey", "https://example.com", "28408e37-5b7d-4c6d-b723-b7a845418dcd", "EUR", "10"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("currency/give", HttpMethod.POST), GiveCurrencyResponse.class);
        GiveCurrencyResponse giveCurrencyResponseDTO = (GiveCurrencyResponse) gatewayResponse.getBody();

        // then
        assertEquals("10 EUR given to 28408e37-5b7d-4c6d-b723-b7a845418dcd", giveCurrencyResponseDTO.getMessage());
    }

    @Test
    public void ioExceptionToClientExceptionTest() throws Exception {
        // given
        doThrow(IOException.class).when(webstoreHTTPClient).sendHttpRequest(any(OkHttpClient.class), any(Request.class));
        ConnectCommand connect = new ConnectCommand();
        connect.validate(new String[]{"secretKey", "https://example.com"});

        // then
        thrown.expect(ClientException.class);
        thrown.expectMessage("Exception when contacting the webstore API");

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("information", HttpMethod.GET), InformationResponse.class);
        InformationResponse informationResponse = (InformationResponse) gatewayResponse.getBody();

    }

    @Test
    public void interruptedExceptionToClientExceptionTest() throws Exception {
        // given
        doThrow(InterruptedException.class).when(webstoreHTTPClient).sendHttpRequest(any(OkHttpClient.class), any(Request.class));
        ConnectCommand connect = new ConnectCommand();
        connect.validate(new String[]{"secretKey", "https://example.com"});

        // then
        thrown.expect(ClientException.class);
        thrown.expectMessage("Exception when contacting the webstore API");

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.sendRequest(buildRequest("information", HttpMethod.GET), InformationResponse.class);
        InformationResponse information = (InformationResponse) gatewayResponse.getBody();
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
