package net.donationstore.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GiveCurrencyResponse {

    @JsonProperty("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public GiveCurrencyResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
