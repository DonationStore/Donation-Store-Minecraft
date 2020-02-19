package net.donationstore.exception;

import java.util.ArrayList;

public class WebstoreAPIException extends Exception {

    public ArrayList<String> logs;

    public WebstoreAPIException(ArrayList<String> logs) {
        this.logs = logs;
    }
}
