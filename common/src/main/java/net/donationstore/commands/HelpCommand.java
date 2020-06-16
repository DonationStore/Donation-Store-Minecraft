package net.donationstore.commands;

import net.donationstore.enums.CommandType;

import java.util.ArrayList;

public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        setPermission("donationstore.help");
    }

    @Override
    public String getSupportedCommand() {
        return "help";
    }

    @Override
    public Command validate(String[] args) {
        return this;
    }

    @Override
    public ArrayList<String> runCommand() {
        getLogs().add("Help: ");
        getLogs().add("/ds balance : Gets your virtual currency balances");
        getLogs().add("/ds code : Generates a virtual currency code");
        getLogs().add("/ds currency <ign> <currency-code> <amount> : Gives that user, that amount of that currency");
        getLogs().add("/ds help : Runs the help command");

        return returnAndClearLogs();
    }

    @Override
    public String helpInfo() {
        return "This command is used to give help and usage for all of the commands found in the Donation Store plugin.";
    }

    @Override
    public CommandType commandType() {
        return CommandType.ANY;
    }
}
