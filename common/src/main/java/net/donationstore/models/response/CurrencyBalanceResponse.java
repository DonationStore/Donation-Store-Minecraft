package net.donationstore.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.UUID;

public class CurrencyBalanceResponse {

    @JsonProperty("username")
    private String username;

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("balances")
    private Map<String, Object> balances;

    public String getUsername() {
        return username;
    }

    public CurrencyBalanceResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CurrencyBalanceResponse setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Map<String, Object> getBalances() {
        return balances;
    }

    public CurrencyBalanceResponse setBalances(Map<String, Object> balances) {
        this.balances = balances;
        return this;
    }
}