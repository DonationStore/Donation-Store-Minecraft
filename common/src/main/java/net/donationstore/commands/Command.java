package net.donationstore.commands;

import java.util.ArrayList;

public interface Command {
    public ArrayList<String> runCommand() throws Exception;

    public String helpInfo();

    public CommandType commandType();
}
