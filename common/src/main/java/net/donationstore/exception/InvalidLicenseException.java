package net.donationstore.exception;

import java.util.ArrayList;

public class InvalidLicenseException extends RuntimeException {

    public ArrayList<String> logs;

    public InvalidLicenseException(ArrayList<String> logs) {
        this.logs = logs;
    }
}
