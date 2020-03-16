package net.donationstore.models.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    @JsonProperty("error")
    private ErrorMessage errorMessage;

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }
}
