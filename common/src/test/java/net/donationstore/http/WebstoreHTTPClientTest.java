package net.donationstore.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.donationstore.commands.*;
import net.donationstore.dto.*;
import net.donationstore.exception.WebstoreAPIException;
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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
    public void generateExceptionTest() {

    }

    @Test
    public void getAPIUrlTest() throws Exception {
        // when
        URI uri = webstoreHTTPClient.getAPIUrl("information");

        // then
        assertEquals(uri.getAuthority(), "example.com");
        assertEquals(uri.getHost(), "example.com");
        assertEquals(uri.getPath(), "/information");
    }

    @Test
    public void connectCommandTest() throws Exception {
        // given
        doReturn("{\"webstore\": {\"currency\": \"EUR\", \"id\": 1, \"name\": \"Example Store\"}, \"server\": {\"ip\": \"127.0.0.1\", \"id\": 1, \"name\": \"Hello World\"}}").when(webstoreHTTPClient).sendHttpRequest(any(HttpClient.class), any(HttpRequest.class));
        ConnectCommand connect = new ConnectCommand();
        connect.validate(new String[]{"connect", "secretKey", "https://example.com"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.get(connect, "information");
        InformationDTO informationDTO = (InformationDTO) gatewayResponse.getBody();

        // then
        assertEquals(informationDTO.webstore.get("currency"), "EUR");
        assertEquals(informationDTO.webstore.get("id"), 1);
        assertEquals(informationDTO.webstore.get("name"), "Example Store");
        assertEquals(informationDTO.server.get("ip"), "127.0.0.1");
        assertEquals(informationDTO.server.get("name"), "Hello World");
    }

    @Test
    public void getCurrencyBalancesTest() throws Exception {
        // given
        doReturn("{\"username\": \"MCxJB\", \"uuid\": \"28408e37-5b7d-4c6d-b723-b7a845418dcd\", \"balances\": {\"EUR\": \"1.00\"}}").when(webstoreHTTPClient).sendHttpRequest(any(HttpClient.class), any(HttpRequest.class));
        GetCurrencyBalancesCommand getCurrencyBalancesCommand = new GetCurrencyBalancesCommand();
        getCurrencyBalancesCommand.validate(new String[]{"balance", "secretKey", "https://example.com", "28408e37-5b7d-4c6d-b723-b7a845418dcd"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.post(getCurrencyBalancesCommand, "currency/balances");
        CurrencyBalanceDTO currencyBalanceDTO = (CurrencyBalanceDTO) gatewayResponse.getBody();

        // then
        assertEquals("MCxJB", currencyBalanceDTO.username);
        assertEquals(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"), currencyBalanceDTO.uuid);
        assertEquals("1.00", currencyBalanceDTO.balances.get("EUR"));
    }

    @Test
    public void getCurrencyCodeTest() throws Exception {
        // given
        doReturn("{\"code\": \"D3CRWAZ47A\", \"uuid\": \"28408e37-5b7d-4c6d-b723-b7a845418dcd\"}").when(webstoreHTTPClient).sendHttpRequest(any(HttpClient.class), any(HttpRequest.class));
        GetCurrencyCodeCommand getCurrencyCodeCommand = new GetCurrencyCodeCommand();
        getCurrencyCodeCommand.validate(new String[]{"code", "secretKey", "https://example.com", "28408e37-5b7d-4c6d-b723-b7a845418dcd"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.post(getCurrencyCodeCommand, "currency/code/generate");
        CurrencyCodeDTO currencyCodeDTO = (CurrencyCodeDTO) gatewayResponse.getBody();

        // then
        assertEquals("D3CRWAZ47A", currencyCodeDTO.code);
        assertEquals(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"), currencyCodeDTO.uuid);
    }

    @Test
    public void giveCurrencyCodeTest() throws Exception {
        // given
        doReturn("{\"message\": \"10 EUR given to 28408e37-5b7d-4c6d-b723-b7a845418dcd\"}").when(webstoreHTTPClient).sendHttpRequest(any(HttpClient.class), any(HttpRequest.class));
        GiveCurrencyCommand giveCurrencyCommand = new GiveCurrencyCommand();
        giveCurrencyCommand.validate(new String[]{"give", "secretKey", "https://example.com", "28408e37-5b7d-4c6d-b723-b7a845418dcd", "EUR", "10"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.post(giveCurrencyCommand, "currency/give");
        GiveCurrencyDTO giveCurrencyDTO = (GiveCurrencyDTO) gatewayResponse.getBody();

        // then
        assertEquals("10 EUR given to 28408e37-5b7d-4c6d-b723-b7a845418dcd", giveCurrencyDTO.message);
    }

    @Test
    public void ioExceptionTest() throws Exception {
        // then
        thrown.expect(WebstoreAPIException.class);
        doThrow(IOException.class).when(webstoreHTTPClient).sendHttpRequest(any(HttpClient.class), any(HttpRequest.class));
        ConnectCommand connect = new ConnectCommand();
        connect.validate(new String[]{"connect", "secretKey", "https://example.com"});

        // when
        GatewayResponse gatewayResponse = webstoreHTTPClient.get(connect, "information");
        InformationDTO informationDTO = (InformationDTO) gatewayResponse.getBody();

    }

    @Test
    public void interruptedExceptionTest() {

    }

    @Test
    public void uriSyntaxExceptionTest() {

    }
}
