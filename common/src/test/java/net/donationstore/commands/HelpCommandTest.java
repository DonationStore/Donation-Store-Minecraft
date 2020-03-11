package net.donationstore.commands;

import net.donationstore.enums.CommandType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class HelpCommandTest {

    private ArrayList<String> logs;

    private HelpCommand helpCommand;

    @Before
    public void setup() {
        logs = new ArrayList<>();
        logs.add("/ds currency-balances : Gets your virtual currency balances");
        logs.add("/ds currency-code : Generates a virtual currency code");
        logs.add("/ds give-currency <ign> <currency-code> <amount> : Gives that user, that amount of that currency");
        logs.add("/ds help : Runs the help command");

        helpCommand = new HelpCommand();
    }

    @Test
    public void gettersTest() {
        // then
        assertEquals("help", helpCommand.getSupportedCommand());
        assertEquals(logs, helpCommand.runCommand());
        assertEquals("This command is used to give help and usage for all of the commands found in the Donation Store plugin.", helpCommand.helpInfo());
        assertEquals(CommandType.ANY, helpCommand.commandType());
    }
}
