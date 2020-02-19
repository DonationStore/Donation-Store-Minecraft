package net.donationstore.commands;

import net.donationstore.exception.InvalidCommandUseException;
import net.donationstore.exception.InvalidLicenseException;
import net.donationstore.license.LicenseHandler;

import java.util.ArrayList;

public class Authorise implements Command {

    private String licenseKey;
    private ArrayList<String> logs;
    private LicenseType licenseType;
    private LicenseHandler licenseHandler;

    public Authorise() {
        logs = new ArrayList<>();
        licenseHandler = new LicenseHandler();
    }

    @Override
    public ArrayList<String> runCommand(String[] args) {

        if (args.length != 1) {
            logs.add("Invalid usage of command. Help Info: ");
            logs.add(this.helpInfo());
            throw new InvalidCommandUseException(logs);
        } else {
            licenseKey = args[1];

            logs.add("Attempting to authorise license.");

            licenseType = licenseHandler.authorise(licenseKey);

            if (licenseType == LicenseType.VALID_LICENSE) {
                logs.add("License was successfully authorised");
                logs.add("License will now be checked on startup and also when retrieving commands from the server");
                logs.add("If you have any queries, contact us at: https://donationstore.net/support");

            } else if (licenseType == LicenseType.INVALID_LICENSE) {
                logs.add("Invalid license. Plugin is now locked.");
                logs.add("Please enter a valid license key or buy one at: https://donationstore.net/buy");

                throw new InvalidLicenseException(logs);
            }
        }

        return logs;
    }

    @Override
    public String helpInfo() {
        return "This command is used to authorise the Donation Store plugin.\n" +
                "Usage: /ds authorise <license_key>";
    }

    @Override
    public CommandType commandType() {
        return CommandType.CONSOLE;
    }
}
