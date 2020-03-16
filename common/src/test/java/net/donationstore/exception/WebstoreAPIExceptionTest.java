package net.donationstore.exception;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class WebstoreAPIExceptionTest {

    private WebstoreAPIException webstoreAPIException;

    @Test
    public void gettersSettersTest() {
        // given
        ArrayList<String> logs = new ArrayList<>();
        logs.add("Example exception message");
        webstoreAPIException = new WebstoreAPIException(logs);

        // then
        assertEquals("Example exception message", webstoreAPIException.getMessage());
    }
}
