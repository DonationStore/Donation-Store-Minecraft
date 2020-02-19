package net.donationstore.exception;

import java.util.ArrayList;

public class InvalidCommandUseException extends RuntimeException {

    public ArrayList<String> logs;

    public InvalidCommandUseException(ArrayList<String> logs) {
        this.logs = logs;
    }
}
