package net.donationstore.commands;

import java.util.HashMap;

public class Manager {

    public HashMap<String, Command> commands;

    public Manager() {
        commands = new HashMap<String, Command>();

        commands.put("help", new HelpCommand());
        commands.put("authorise", new Authorise());
        commands.put("connect", new Connect());
        commands.put("get currency code", new GetCurrencyCodeCommand());
        commands.put("give currency", new GiveCurrencyCommand());
        commands.put("get currency balances", new GetCurrencyBalancesCommand());
    }
}
