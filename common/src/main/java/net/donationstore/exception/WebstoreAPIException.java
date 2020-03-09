package net.donationstore.exception;

import java.util.List;

public class WebstoreAPIException extends Exception {

    public List<String> logs;

    public WebstoreAPIException(List<String> logs) {
        this.logs = logs;
    }

    @Override
    public String getMessage() {
        return this.logs.toString();
    }
}
