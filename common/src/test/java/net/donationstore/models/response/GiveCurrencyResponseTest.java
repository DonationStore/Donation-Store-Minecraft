package net.donationstore.models.response;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GiveCurrencyResponseTest {

    private GiveCurrencyResponse giveCurrencyResponse;

    @Test
    public void gettersSettersTest() {
        // given
        giveCurrencyResponse = new GiveCurrencyResponse();
        giveCurrencyResponse.setMessage("Successfully gave 10 EUR to MCxJB");

        // then
        assertEquals("Successfully gave 10 EUR to MCxJB", giveCurrencyResponse.getMessage());
    }
}
