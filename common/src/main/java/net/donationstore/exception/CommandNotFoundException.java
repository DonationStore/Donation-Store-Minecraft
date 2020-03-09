package net.donationstore.exception;

import java.util.List;

public class CommandNotFoundException extends RuntimeException {

    public List<String> logs;

    public CommandNotFoundException(List<String> logs) {
        this.logs = logs;
    }

    @Override
    public String getMessage() {
        return this.logs.toString();
    }
}
