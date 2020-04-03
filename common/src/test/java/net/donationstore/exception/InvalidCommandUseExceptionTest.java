package net.donationstore.exception;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class InvalidCommandUseExceptionTest {

    private InvalidCommandUseException invalidCommandUseException;

    @Test
    public void gettersSettersTest() {
        // given
        ArrayList<String> logs = new ArrayList<>();
        logs.add("Example exception message");
        invalidCommandUseException = new InvalidCommandUseException(logs);

        // then
        assertEquals("Example exception message", invalidCommandUseException.getMessage());
    }
}
