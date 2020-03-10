package net.donationstore.commands;

import net.donationstore.enums.CommandType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AbstractCommandTest {

    public class TestCommand extends AbstractCommand {

        @Override
        public String getSupportedCommand() {
            return "example";
        }

        @Override
        public Command validate(String[] args) {
            return null;
        }

        @Override
        public CommandType commandType() {
            return CommandType.PLAYER;
        }

        @Override
        public ArrayList<String> runCommand() throws Exception {
            return null;
        }

        @Override
        public String helpInfo() {
            return "Example help info";
        }
    }

    private TestCommand testCommand;

    @Before
    public void setup() {
        testCommand = new TestCommand();
    }

    @Test
    public void gettersSettersTest() {
        // then
        assertEquals(CommandType.PLAYER, testCommand.commandType());
        assertEquals("Example help info", testCommand.helpInfo());
        assertEquals("Invalid usage of command. Help Info: ", testCommand.getInvalidCommandMessage());
    }

    @Test
    public void addLogTest() {
        // given
        ArrayList<String> testLogs = new ArrayList<>();
        testLogs.add("Example log");

        // when
        testCommand.addLog("Example log");

        // then
        assertEquals(testLogs, testCommand.getLogs());
    }
}
