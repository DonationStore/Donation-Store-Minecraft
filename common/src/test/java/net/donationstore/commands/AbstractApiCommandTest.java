package net.donationstore.commands;

import net.donationstore.enums.CommandType;
import net.donationstore.enums.HttpMethod;
import net.donationstore.models.request.CurrencyCodeRequest;
import net.donationstore.models.request.GatewayRequest;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AbstractApiCommandTest<T> {

    public class TestCommand extends AbstractApiCommand<T> {

        @Override
        public String getSupportedCommand() {
            return "test";
        }

        @Override
        public Command validate(String[] args) {
            return null;
        }

        @Override
        public CommandType commandType() {
            return CommandType.PLAYER;
        }

        @Override
        public ArrayList<String> runCommand() throws Exception {
            return null;
        }

        @Override
        public String helpInfo() {
            return "Example help info";
        }
    }

    private TestCommand testCommand;

    @Before
    public void setup() {
        testCommand = new TestCommand();
    }

    @Test
    public void gettersSettersTest() {
        // given
        testCommand.getWebstoreHTTPClient().setSecretKey("example");
        testCommand.getWebstoreHTTPClient().setWebstoreAPILocation("https://example.com");

        // then
        assertEquals("example", testCommand.getWebstoreHTTPClient().getSecretKey());
        assertEquals("https://example.com", testCommand.getWebstoreHTTPClient().getWebstoreAPILocation());
        assertEquals("test", testCommand.getSupportedCommand());
        assertEquals(CommandType.PLAYER, testCommand.commandType());
        assertEquals("Example help info", testCommand.helpInfo());
        assertEquals("Invalid usage of command. Help Info: ", testCommand.getInvalidCommandMessage());
    }

    @Test
    public void defaultHeadersTest() {
        // given
        testCommand.getWebstoreHTTPClient().setSecretKey("example");
        Map<String, String> headers = new HashMap<>();
        headers.put("Secret-Key", "example");

        // then
        assertEquals(headers, testCommand.getWebstoreHTTPClient().getDefaultHeaders());
    }

    @Test
    public void buildDefaultRequestTest() throws Exception {
        // given
        testCommand.getWebstoreHTTPClient().setSecretKey("example");
        testCommand.getWebstoreHTTPClient().setWebstoreAPILocation("https://example.com");
        testCommand.getWebstoreHTTPClient().setSecretKey("example");
        Map<String, String> headers = new HashMap<>();
        headers.put("Secret-Key", "example");
        CurrencyCodeRequest currencyCodeRequest = new CurrencyCodeRequest();

        // when
        GatewayRequest request = testCommand.getWebstoreHTTPClient().buildDefaultRequest("example", HttpMethod.POST, currencyCodeRequest);

        // then
        assertEquals(URI.create("https://example.com/example"), request.getUri());
        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals(headers, request.getHeaders());
    }
}
