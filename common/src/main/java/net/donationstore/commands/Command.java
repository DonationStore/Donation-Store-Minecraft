package net.donationstore.commands;

import java.util.ArrayList;

public interface Command {

    Command validate(String[] args);

    String getSupportedCommand();

    public ArrayList<String> runCommand() throws Exception;

    public String helpInfo();

    public CommandType commandType();
}
