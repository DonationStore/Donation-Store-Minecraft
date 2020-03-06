package net.donationstore.exception;

import java.util.List;

public class InvalidCommandUseException extends RuntimeException {

    public List<String> logs;

    public InvalidCommandUseException(List<String> logs) {
        this.logs = logs;
    }

    @Override
    public String getMessage() {
        return this.logs.toString();
    }
}
