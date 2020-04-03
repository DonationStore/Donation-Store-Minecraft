package net.donationstore.exception;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CommandNotFoundExceptionTest {

    private CommandNotFoundException commandNotFoundException;

    @Test
    public void gettersSettersTest() {
        // given
        ArrayList<String> logs = new ArrayList<>();
        logs.add("Example exception message");
        commandNotFoundException = new CommandNotFoundException(logs);

        // then
        assertEquals("Example exception message", commandNotFoundException.getMessage());
    }
}
