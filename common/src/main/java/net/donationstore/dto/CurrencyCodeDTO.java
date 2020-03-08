package net.donationstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CurrencyCodeDTO {

    @JsonProperty("code")
    public String code;

    @JsonProperty("uuid")
    public UUID uuid;
}
