package net.donationstore.commands;

import java.util.ArrayList;

public class HelpCommand extends AbstractCommand {

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
        getLogs().add("/ds currency-balances : Gets your virtual currency balances");
        getLogs().add("/ds currency-code : Generates a virtual currency code");
        getLogs().add("/ds give-currency <ign> <currency-code> <amount> : Gives that user, that amount of that currency");
        getLogs().add("/ds help : Runs the help command");

        return getLogs();
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
