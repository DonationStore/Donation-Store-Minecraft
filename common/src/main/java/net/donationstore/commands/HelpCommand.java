package net.donationstore.commands;

import java.util.ArrayList;

public class HelpCommand implements Command {

    private ArrayList<String> logs;

    public HelpCommand() {
        logs = new ArrayList<>();
    }

    @Override
    public ArrayList<String> runCommand(String[] args) {
        // Just add things to the help page and return it
        return null;
    }

    @Override
    public String helpInfo() {
        return "This command is used to give help and usage for all of the commands found in the Donation Store plugin.";
    }

    @Override
    public CommandType commandType() {
        return CommandType.ANY;
    }
}
