package net.donationstore.models.response;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateCommandExecutedResponseTest {

    private UpdateCommandExecutedResponse updateCommandExecutedResponse;

    @Before
    public void setup() {
        updateCommandExecutedResponse = new UpdateCommandExecutedResponse();
    }

    @Test
    public void gettersSettersTest() {
        // given
        ArrayList<Integer> commandsNotFound = new ArrayList<>();
        commandsNotFound.add(1);
        commandsNotFound.add(2);
        updateCommandExecutedResponse.setMessage("Example Message").setCommandsNotFound(Optional.of(commandsNotFound));

        // then
        assertEquals("Example Message", updateCommandExecutedResponse.getMessage());
        assertTrue(updateCommandExecutedResponse.getCommandsNotFound().isPresent());
        assertEquals(commandsNotFound, updateCommandExecutedResponse.getCommandsNotFound().get());
    }

    @Test
    public void notFoundCommandsOptionalTest() {
        // given
        updateCommandExecutedResponse.setMessage("Example Message");

        // then
        assertTrue(updateCommandExecutedResponse.getCommandsNotFound().isEmpty());
    }
}
