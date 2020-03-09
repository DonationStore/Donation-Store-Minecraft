package net.donationstore.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.UUID;

public class CurrencyBalanceResponse {

    @JsonProperty("username")
    public String username;

    @JsonProperty("uuid")
    public UUID uuid;

    @JsonProperty("balances")
    public Map<String, Object> balances;
}