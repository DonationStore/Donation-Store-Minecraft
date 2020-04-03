package net.donationstore.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Optional;

public class UpdateCommandExecutedResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("commands_not_found")
    private Optional<ArrayList<Integer>> commandsNotFound;

    public UpdateCommandExecutedResponse() {
        commandsNotFound = Optional.empty();
    }

    public String getMessage() {
        return message;
    }

    public UpdateCommandExecutedResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public Optional<ArrayList<Integer>> getCommandsNotFound() {
        return commandsNotFound;
    }

    public UpdateCommandExecutedResponse setCommandsNotFound(Optional<ArrayList<Integer>> commandsNotFound) {
        this.commandsNotFound = commandsNotFound;
        return this;
    }
}
