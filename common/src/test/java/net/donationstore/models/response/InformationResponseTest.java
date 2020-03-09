package net.donationstore.models.response;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class InformationResponseTest {

    private InformationResponse informationResponse;

    @Test
    public void gettersSettersTest() {
        // given
        Map<String, Object> webstore = new HashMap<>();
        Map<String, Object> server = new HashMap<>();
        Map<String, String> webstoreValues = new HashMap<>();
        Map<String, String> serverValues = new HashMap<>();
        webstoreValues.put("currency", "EUR");
        webstoreValues.put("id", "1");
        webstoreValues.put("name", "Example Store");
        serverValues.put("ip", "127.0.0.1");
        serverValues.put("id", "1");
        serverValues.put("name", "Hello World");
        webstore.put("webstore", webstoreValues);
        server.put("server", serverValues);

        informationResponse = new InformationResponse();
        informationResponse.setWebstore(webstore);
        informationResponse.setServer(server);

        // then
        assertEquals(webstore, informationResponse.getWebstore());
        assertEquals(server, informationResponse.getServer());
    }
}
