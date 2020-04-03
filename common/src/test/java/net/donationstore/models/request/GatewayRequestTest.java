package net.donationstore.models.request;

import net.donationstore.enums.HttpMethod;
import org.junit.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GatewayRequestTest {

    private GatewayRequest gatewayRequest;

    @Test
    public void gettersSettersTest() {
        // given
        CurrencyCodeRequest currencyCodeRequest = new CurrencyCodeRequest();
        currencyCodeRequest.setUuid("28408e37-5b7d-4c6d-b723-b7a845418dcd");
        Map<String, String> headers = new HashMap<>();
        headers.put("Secret-Key", "example");

        gatewayRequest = new GatewayRequest();
        gatewayRequest.setUri(URI.create("https://example.com/api"));
        gatewayRequest.setBody(currencyCodeRequest);
        gatewayRequest.setMethod(HttpMethod.POST);
        gatewayRequest.setHeaders(headers);

        // then
        assertEquals(URI.create("https://example.com/api"), gatewayRequest.getUri());
        assertEquals(currencyCodeRequest, gatewayRequest.getBody());
        assertEquals(HttpMethod.POST, gatewayRequest.getMethod());
        assertEquals(headers, gatewayRequest.getHeaders());
    }
}
