package net.donationstore.exception;

import java.util.ArrayList;

public class CommandNotFoundException extends RuntimeException {

    public ArrayList<String> logs;

    public CommandNotFoundException(ArrayList<String> logs) {
        this.logs = logs;
    }

    @Override
    public String getMessage() {
        return this.logs.toString();
    }
}
