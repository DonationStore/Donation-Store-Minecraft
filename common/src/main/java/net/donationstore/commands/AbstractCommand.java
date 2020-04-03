package net.donationstore.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.donationstore.enums.CommandType;

import java.util.ArrayList;

public abstract class AbstractCommand implements Command {

    @JsonIgnore
    private ArrayList<String> logs;

    public AbstractCommand() {
        logs = new ArrayList<>();
    }

    @JsonIgnore
    private String permission;

    @JsonIgnore
    public abstract String getSupportedCommand();

    public abstract Command validate(String[] args);

    public abstract CommandType commandType();

    public abstract ArrayList<String> runCommand() throws Exception;

    @JsonIgnore
    public abstract String helpInfo();

    @JsonIgnore
    public String getInvalidCommandMessage() {
        return "Invalid usage of command. Help Info: ";
    }

    public void addLog(String log) {
        logs.add(log);
    }

    public ArrayList<String> getLogs() {
        return logs;
    }

    public ArrayList<String> returnAndClearLogs() {
        ArrayList<String> logs = (ArrayList)getLogs().clone();
        getLogs().clear();
        return logs;
    }

    public String getPermission() {
        return permission;
    }

    public AbstractCommand setPermission(String permission) {
        this.permission = permission;
        return this;
    }
}
