package net.donationstore.http;

import net.donationstore.commands.Command;
import net.donationstore.dto.WebstoreAPIResponseDTO;

import java.net.http.HttpClient;

public class WebstoreHTTPClient {

    private String secretKey;
    private HttpClient httpClient;
    private String webstoreAPILocation;

    public WebstoreHTTPClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public WebstoreHTTPClient setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public WebstoreHTTPClient webstoreHTTPClient(String webstoreAPILocation) {
        this.webstoreAPILocation = webstoreAPILocation;
        return this;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getWebstoreAPILocation() {
        return this.webstoreAPILocation;
    }

    /*
     Command comes in as a parameter. Generic stuff that should be passed to the API are set first for all commands
     So the secretKey and the webstoreAPILocation

     Then, use Jackson to map the command object (which has JSON property annotations) to a string.

     Ask => Does the @JsonProperty mean that when this object is changed to JSON, they are the fields that are coming with
     it.
     */
    public WebstoreAPIResponseDTO get(Command command) {

    }

    public WebstoreAPIResponseDTO post(Command command) {

    }
}
