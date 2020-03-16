package net.donationstore.commands;

import net.donationstore.exception.InvalidCommandUseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CommandFactoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandFactory commandFactory;

    @Before
    public void setup() {
        commandFactory = new CommandFactory();
    }

    @Test
    public void setupTest() {
        // given
        List<Command> commandList = (List<Command>) ReflectionTestUtils.getField(commandFactory, "commandList");

        // then
        assertTrue(commandList.stream().anyMatch(e -> e instanceof HelpCommand));
        assertTrue(commandList.stream().anyMatch(e -> e instanceof ConnectCommand));
        assertTrue(commandList.stream().anyMatch(e -> e instanceof GetCurrencyBalancesCommand));
        assertTrue(commandList.stream().anyMatch(e -> e instanceof GetCurrencyCodeCommand));
        assertTrue(commandList.stream().anyMatch(e -> e instanceof GiveCurrencyCommand));
    }

    @Test
    public void whenCommandExistsWithCorrectArgumentsTest() {
        // given
        Command command = commandFactory.getCommand(new String[]{"balance", "webstoreAPILocation", "secretKey", "28408e37-5b7d-4c6d-b723-b7a845418dcd"});

        // then
        assertTrue(command instanceof GetCurrencyBalancesCommand);
    }

    @Test
    public void whenCommandExistsWithIncorrectArgumentsTest() {
        // then
        thrown.expect(InvalidCommandUseException.class);
        thrown.expectMessage("Invalid usage of command. Help Info: This command is used to view your Virtual Currency balances.\n Usage: /ds balance");

        // when
        Command command = commandFactory.getCommand(new String[]{"balance", "webstoreAPILocation", "secretKey"});
    }

    @Test
    public void whenCommandDoesNotExistTest() {
        // then
        thrown.expect(InvalidCommandUseException.class);
        thrown.expectMessage("That command doesn't exist.");

        // when
        Command command = commandFactory.getCommand(new String[]{"incorrectCommand", "webstoreAPILocation", "secretKey"});
    }
}
