package net.donationstore.dto;

import java.util.HashMap;

public class CommandExectionPayloadDTO {
    // ID -> Command
    public HashMap<String, String> commands;

    public HashMap<String, String> meta;

    public HashMap<String, String> variables;

    public CommandExectionPayloadDTO() {
        commands = new HashMap<>();
        meta = new HashMap<>();
        variables = new HashMap<String, String>();
    }
}
