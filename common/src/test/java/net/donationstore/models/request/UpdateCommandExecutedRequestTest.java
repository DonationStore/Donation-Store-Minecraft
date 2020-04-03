package net.donationstore.models.request;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UpdateCommandExecutedRequestTest {

    private UpdateCommandExecutedRequest updateCommandExecutedRequest;

    @Test
    public void gettersSettersTest() {
        // given
        updateCommandExecutedRequest = new UpdateCommandExecutedRequest();
        ArrayList<Integer> commands = new ArrayList<>();
        commands.add(1);
        commands.add(2);
        updateCommandExecutedRequest.setCommands(commands);

        // then
        assertEquals(commands, updateCommandExecutedRequest.getCommands());
    }
}
