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
        giveCurrencyRequest.setUsername("MCxJB");
        giveCurrencyRequest.setCurrencyCode("EUR");

        // then
        assertEquals("100", giveCurrencyRequest.getAmount());
        assertEquals("MCxJB", giveCurrencyRequest.getUsername());
        assertEquals("EUR", giveCurrencyRequest.getCurrencyCode());
    }
}
