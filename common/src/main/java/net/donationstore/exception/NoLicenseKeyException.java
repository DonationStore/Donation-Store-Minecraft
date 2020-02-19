package net.donationstore.exception;

import java.util.ArrayList;

public class NoLicenseKeyException extends RuntimeException {

    public ArrayList<String> logs;

    public NoLicenseKeyException(ArrayList<String> logs) {
        this.logs = logs;
    }
}
