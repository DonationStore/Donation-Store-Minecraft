package net.donationstore.models.response;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class GatewayResponseTest {

    private GatewayResponse gatewayResponse;

    @Test
    public void gettersSettersTest() {
        // given
        CurrencyCodeResponse currencyCodeResponse = new CurrencyCodeResponse();
        currencyCodeResponse.setCode("DBSHADBAU92");
        currencyCodeResponse.setUuid(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"));
        gatewayResponse = new GatewayResponse();
        gatewayResponse.setBody(currencyCodeResponse);

        // then
        assertEquals(currencyCodeResponse, gatewayResponse.getBody());
    }
}
