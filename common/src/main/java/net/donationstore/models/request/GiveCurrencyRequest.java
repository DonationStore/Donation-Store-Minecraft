package net.donationstore.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GiveCurrencyRequest {
    @JsonProperty("amount")
    private String amount;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("currency-code")
    private String currencyCode;

    public String getAmount() {
        return amount;
    }

    public GiveCurrencyRequest setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public GiveCurrencyRequest setUuid(String uuid) {
        this.uuid = uuid;
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
