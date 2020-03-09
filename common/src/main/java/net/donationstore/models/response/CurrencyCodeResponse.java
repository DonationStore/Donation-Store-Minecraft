package net.donationstore.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CurrencyCodeResponse {

    @JsonProperty("code")
    private String code;

    @JsonProperty("uuid")
    private UUID uuid;

    public String getCode() {
        return code;
    }

    public CurrencyCodeResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CurrencyCodeResponse setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }
}
