package net.donationstore.commands;

import net.donationstore.exception.InvalidCommandUseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandFactory {

    private List<String> logs;

    private List<Command> commandList;

    public CommandFactory() {
        logs = new ArrayList<>();
        commandList = Arrays.asList(
                new HelpCommand(),
                new ConnectCommand(),
                new GetCurrencyBalancesCommand(),
                new GetCurrencyCodeCommand(),
                new GiveCurrencyCommand()
        );
    }

    public Command getCommand(String[] args) {
        String command = args[0];

        Optional<Command> commandRunner = commandList.stream().filter(c -> c.getSupportedCommand().equals(command)).findFirst();
        return commandRunner.map(
                c -> c.validate(Arrays.copyOfRange(args, 1, args.length))).<InvalidCommandUseException>orElseThrow(() -> {
            logs.clear();
            logs.add("That command doesn't exist.");
            throw new InvalidCommandUseException(logs);
        });
    }
}
