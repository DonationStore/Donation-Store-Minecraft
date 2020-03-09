package net.donationstore.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyCodeRequest {
    @JsonProperty("uuid")
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public CurrencyCodeRequest setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}
