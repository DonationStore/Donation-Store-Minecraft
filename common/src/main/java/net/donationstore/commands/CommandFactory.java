package net.donationstore.commands;

import net.donationstore.exception.InvalidCommandUseException;

import java.util.ArrayList;

public class CommandFactory {

    private static final String HELP = "help";
    private static final String CODE = "code";
    private static final String CONNECT = "connect";
    private static final String BALANCE = "balance";
    private static final String CURRENCY = "currency";
    private static final String HELP_MESSAGE = "Invalid usage of command. Help Info: ";

    private ArrayList<String> logs;

    public CommandFactory() {
        logs = new ArrayList<>();
    }

    public Command getCommand(String[] args) {

        String command = args[0];

        switch (command) {
            case HELP:
                return new HelpCommand();
            case CONNECT:
                ConnectCommand connect = new ConnectCommand();

                // If the min args that a command takes is 3, should this be < 3 && max being like 4 > 4
                // Then specifically go into detail in each one and say hey this isn't correct
                if (args.length != 3) {
                    logs.add(HELP_MESSAGE);
                    logs.add(connect.helpInfo());
                    throw new InvalidCommandUseException(logs);
                }

                connect.setSecretKey(args[1])
                        .setWebstoreAPILocation(args[2]);

                return connect;
            case BALANCE:
                GetCurrencyBalancesCommand currencyBalancesCommand = new GetCurrencyBalancesCommand();

                if (args.length != 3) {
                    logs.add(HELP_MESSAGE);
                    logs.add(currencyBalancesCommand.helpInfo());
                    throw new InvalidCommandUseException(logs);
                }

                currencyBalancesCommand.setSecretKey(args[1])
                        .setWebstoreAPILocation(args[2])
                        .setUsername(args[3]);

                return currencyBalancesCommand;
            case CODE:
                GetCurrencyCodeCommand currencyCodeCommand = new GetCurrencyCodeCommand();

                if (args.length != 3) {
                    logs.add(HELP_MESSAGE);
                    logs.add(currencyCodeCommand.helpInfo());
                    throw new InvalidCommandUseException(logs);
                }

                currencyCodeCommand.setSecretKey(args[1])
                        .setWebstoreAPILocation(args[2])
                        .setUsername(args[3]);

                return currencyCodeCommand;
            case CURRENCY:
                GiveCurrencyCommand giveCurrencyCommand = new GiveCurrencyCommand();

                if (args.length != 5) {
                    logs.add(HELP_MESSAGE);
                    logs.add(giveCurrencyCommand.helpInfo());
                    throw new InvalidCommandUseException(logs);
                }

                giveCurrencyCommand.setSecretKey(args[1])
                        .setWebstoreAPILocation(args[2])
                        .setCurrencyCode(args[3])
                        .setAmount(args[4])
                        .setUsername(args[5]);

                return giveCurrencyCommand;
        }

        logs.add("That command doesn't exist.");
        throw new InvalidCommandUseException(logs);
    }
}
