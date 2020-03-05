package net.donationstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.UUID;

public class CurrencyBalanceDTO implements WebstoreAPIResponseDTO {

    @JsonProperty("username")
    private String username;

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("balances")
    private Map<String, Object> balances;
}
