package net.donationstore.commands;

import java.util.ArrayList;

public class HelpCommand implements Command {

    private ArrayList<String> logs;

    public HelpCommand() {
        logs = new ArrayList<>();
    }

    @Override
    public ArrayList<String> runCommand(String[] args) {
        logs.add("/ds currency-balances : Gets your virtual currency balances");
        logs.add("/ds currency-code : Generates a virtual currency code");
        logs.add("/ds give-currency <ign> <currency-code> <amount> : Gives that user, that amount of that currency");
        logs.add("/ds help : Runs the help command");

        return logs;
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
