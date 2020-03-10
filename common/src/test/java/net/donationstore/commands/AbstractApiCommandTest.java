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
        testCommand.setSecretKey("example");
        testCommand.setWebstoreApiLocation("https://example.com");

        // then
        assertEquals("example", testCommand.getSecretKey());
        assertEquals("https://example.com", testCommand.getWebstoreApiLocation());
        assertEquals("test", testCommand.getSupportedCommand());
        assertEquals(CommandType.PLAYER, testCommand.commandType());
        assertEquals("Example help info", testCommand.helpInfo());
        assertEquals("Invalid usage of command. Help Info: ", testCommand.getInvalidCommandMessage());
    }

    @Test
    public void defaultHeadersTest() {
        // given
        testCommand.setSecretKey("example");
        Map<String, String> headers = new HashMap<>();
        headers.put("secret-key", "example");

        // then
        assertEquals(headers, testCommand.getDefaultHeaders());
    }

    @Test
    public void buildDefaultRequestTest() throws Exception {
        // given
        testCommand.setSecretKey("example");
        testCommand.setWebstoreApiLocation("https://example.com");
        testCommand.setSecretKey("example");
        Map<String, String> headers = new HashMap<>();
        headers.put("secret-key", "example");
        CurrencyCodeRequest currencyCodeRequest = new CurrencyCodeRequest();

        // when
        GatewayRequest request = testCommand.buildDefaultRequest("example", HttpMethod.POST, currencyCodeRequest);

        // then
        assertEquals(URI.create("https://example.com/example"), request.getUri());
        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals(headers, request.getHeaders());
    }
}
