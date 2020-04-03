package net.donationstore.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GiveCurrencyRequest {
    @JsonProperty("amount")
    private String amount;

    @JsonProperty("username")
    private String username;

    @JsonProperty("currency_code")
    private String currencyCode;

    public String getAmount() {
        return amount;
    }

    public GiveCurrencyRequest setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public GiveCurrencyRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public GiveCurrencyRequest setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }
}
