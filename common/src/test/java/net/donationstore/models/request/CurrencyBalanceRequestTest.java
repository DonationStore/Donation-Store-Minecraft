package net.donationstore.models.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CurrencyBalanceRequestTest {

    private CurrencyBalanceRequest currencyBalanceRequest;

    @Test
    public void gettersSettersTest() {
        // given
        currencyBalanceRequest = new CurrencyBalanceRequest();
        currencyBalanceRequest.setUuid("28408e37-5b7d-4c6d-b723-b7a845418dcd");

        // then
        assertEquals("28408e37-5b7d-4c6d-b723-b7a845418dcd", currencyBalanceRequest.getUuid());
    }
}
