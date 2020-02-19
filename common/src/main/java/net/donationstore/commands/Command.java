package net.donationstore.commands;

import java.util.ArrayList;

public interface Command {
    // Return list that the logs are printed from
    // Throw exception if its an exception with a list as it's value
    // Print the logs

    // Don't need to say "Console or Player" If the command returns the console type then run in console etc.
    public ArrayList<String> runCommand(String[] args) throws Exception;

    public String helpInfo();

    public CommandType commandType();
}
