package net.donationstore.models.response;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CurrencyBalanceResponseTest {

    private CurrencyBalanceResponse currencyBalanceResponse;

    @Test
    public void gettersSettersTest() {
        // given
        Map<String, Object> balances = new HashMap<>();
        Map<String, String> currencyBalances = new HashMap<>();
        currencyBalances.put("EUR", "1.00");
        balances.put("balances", currencyBalances);

        currencyBalanceResponse = new CurrencyBalanceResponse();
        currencyBalanceResponse.setUsername("MCxJB");
        currencyBalanceResponse.setUuid(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"));
        currencyBalanceResponse.setBalances(balances);

        // then
        assertEquals("MCxJB", currencyBalanceResponse.getUsername());
        assertEquals(UUID.fromString("28408e37-5b7d-4c6d-b723-b7a845418dcd"), currencyBalanceResponse.getUuid());
        assertEquals(balances, currencyBalanceResponse.getBalances());
    }
}
