package net.donationstore.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class UpdateCommandExecutedRequest {
    @JsonProperty("commands")
    private ArrayList<Integer> commands;

    public UpdateCommandExecutedRequest() {
        commands = new ArrayList<>();
    }

    public ArrayList<Integer> getCommands() {
        return commands;
    }

    public UpdateCommandExecutedRequest setCommands(ArrayList<Integer> commands) {
        this.commands = commands;
        return this;
    }
}
