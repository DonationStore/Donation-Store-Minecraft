package net.donationstore.exception;

import java.util.List;

public class InvalidCommandUseException extends RuntimeException {

    public List<String> logs;

    public InvalidCommandUseException(List<String> logs) {
        this.logs = logs;
    }

    @Override
    public String getMessage() {
        StringBuilder log = new StringBuilder();

        for (String singleLog: logs) {
                log.append(singleLog);
        }

        return log.toString();
    }
}
