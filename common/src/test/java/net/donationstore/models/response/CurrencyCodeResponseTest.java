package net.donationstore.models.response;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CurrencyCodeResponseTest {

    private CurrencyCodeResponse currencyCodeResponse;

    @Test
    public void gettersSettersTest() {
        // given
        currencyCodeResponse = new CurrencyCodeResponse();
        currencyCodeResponse.setCode("DBSHADBAU92");
        currencyCodeResponse.setUuid(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"));

        // then
        assertEquals("DBSHADBAU92", currencyCodeResponse.getCode());
        assertEquals(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"), currencyCodeResponse.getUuid());
    }
}
