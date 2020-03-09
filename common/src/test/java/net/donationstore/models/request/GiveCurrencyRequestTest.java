package net.donationstore.models.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GiveCurrencyRequestTest {

    private GiveCurrencyRequest giveCurrencyRequest;

    @Test
    public void gettersSettersTest() {
        // given
        giveCurrencyRequest = new GiveCurrencyRequest();
        giveCurrencyRequest.setAmount("100");
        giveCurrencyRequest.setUuid("28408e37-5b7d-4c6d-b723-b7a845418dcd");
        giveCurrencyRequest.setCurrencyCode("EUR");

        // then
        assertEquals("100", giveCurrencyRequest.getAmount());
        assertEquals("28408e37-5b7d-4c6d-b723-b7a845418dcd", giveCurrencyRequest.getUuid());
        assertEquals("EUR", giveCurrencyRequest.getCurrencyCode());
    }
}
