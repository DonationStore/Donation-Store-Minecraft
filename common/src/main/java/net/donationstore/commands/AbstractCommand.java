package net.donationstore.commands;

import java.util.ArrayList;

public abstract class AbstractCommand implements Command {

    private ArrayList<String> logs;

    public AbstractCommand() {
        logs = new ArrayList<>();
    }

    public abstract String getSupportedCommand();

    public abstract Command validate(String[] args);

    public abstract CommandType commandType();

    public abstract ArrayList<String> runCommand() throws Exception;

    public abstract String helpInfo();

    public String getInvalidCommandMessage() {
        return "Invalid usage of command. Help Info: ";
    }

    public ArrayList<String> getLogs() {
        return logs;
    }
}
