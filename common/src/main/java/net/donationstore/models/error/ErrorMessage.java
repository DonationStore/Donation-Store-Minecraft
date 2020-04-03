package net.donationstore.models.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {
    @JsonProperty("message")
    private String message;

    @JsonProperty("type")
    private String type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
