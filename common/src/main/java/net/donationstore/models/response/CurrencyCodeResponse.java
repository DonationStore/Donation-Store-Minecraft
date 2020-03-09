package net.donationstore.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CurrencyCodeResponse {

    @JsonProperty("code")
    public String code;

    @JsonProperty("uuid")
    public UUID uuid;
}
