package net.donationstore.commands;

import net.donationstore.exception.InvalidCommandUseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class ConnectTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @InjectMocks
    private ConnectCommand connectCommand;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        connectCommand = new ConnectCommand();
    }

    @Test
    public void verifyConstruction() {
        // then
        assertTrue(((ArrayList)ReflectionTestUtils.getField(connectCommand, "logs")).isEmpty());
    }

    @Test
    public void whenInvalidNumberOfArgumentsGiven() throws Exception {
        // given
        ArrayList<String> logs = new ArrayList<>();
        logs.add("Invalid usage of command. Help Info: ");
        logs.add(connectCommand.helpInfo());
        thrown.expect(InvalidCommandUseException.class);
        thrown.expectMessage(logs.toString());

        // when
        connectCommand.runCommand(new String[] { "One", "Two", "Three" });
    }

    @Test
    public void whenInvalidResponseFromAPI() throws Exception {

    }

    @Test
    public void whenAPIThrowsIOException() {

    }

    @Test
    public void whenAPIThrowsInterruptedException() {

    }
}
