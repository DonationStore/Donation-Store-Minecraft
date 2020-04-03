package net.donationstore.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyBalanceRequest {
    @JsonProperty("uuid")
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public CurrencyBalanceRequest setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}
