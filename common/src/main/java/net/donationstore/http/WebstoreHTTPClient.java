package net.donationstore.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.donationstore.commands.AbstractApiCommand;
import net.donationstore.commands.Command;
import net.donationstore.dto.GatewayResponse;
import net.donationstore.dto.WebstoreAPIResponseDTO;
import net.donationstore.exception.WebstoreAPIException;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class WebstoreHTTPClient {

    private String secretKey;
    public HttpClient httpClient;
    private ArrayList<String> logs;
    public ObjectMapper objectMapper;
    private String webstoreAPILocation;

    public WebstoreHTTPClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        objectMapper = new ObjectMapper();
        logs = new ArrayList<>();
    }

    public WebstoreHTTPClient setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public WebstoreHTTPClient setWebstoreAPILocation(String webstoreAPILocation) {
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
    // This is responsible for taking a command, executing it and returning back a gateway response (or webstore API response API response) as the object
    public GatewayResponse get(AbstractApiCommand command, String resource) throws WebstoreAPIException {

        GatewayResponse gatewayResponse = new GatewayResponse();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(getAPIUrl(resource))
                    .header("Secret-Key", secretKey)
                    .build();

            gatewayResponse.setBody(objectMapper.readValue(sendHttpRequest(httpClient, request), command.getWebstoreAPIResponseDTO()));
        } catch (IOException exception) {
            throw generateException("IOException", command.getSupportedCommand(), exception);
        } catch (InterruptedException exception) {
            throw generateException("InterruptedException", command.getSupportedCommand(), exception);
        } catch(URISyntaxException exception) {
            throw generateException("URISyntaxException", command.getSupportedCommand(), exception);
        }

        return gatewayResponse;
    }

    public GatewayResponse post(AbstractApiCommand command, String resource) throws WebstoreAPIException {

        GatewayResponse gatewayResponse = new GatewayResponse();

        try {
            String requestBody = objectMapper.writeValueAsString(command);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(getAPIUrl(resource))
                    .header("Secret-Key", secretKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            gatewayResponse.setBody(objectMapper.readValue(sendHttpRequest(httpClient, request), command.getWebstoreAPIResponseDTO()));
        } catch (IOException exception) {
            throw generateException("IOException", command.getSupportedCommand(), exception);
        } catch (InterruptedException exception) {
            throw generateException("InterruptedException", command.getSupportedCommand(), exception);
        } catch(URISyntaxException exception) {
            throw generateException("URISyntaxException", command.getSupportedCommand(), exception);
        }

        return gatewayResponse;
    }

    public WebstoreAPIException generateException(String exceptionType, String command, Exception exception) {
        logs.add(String.format("%s exception when contacting the webstore API when running the %s command", exceptionType, command));
        logs.add(ExceptionUtils.getStackTrace(exception));
        return new WebstoreAPIException(logs);
    }

    public URI getAPIUrl(String resource) throws URISyntaxException {
        return new URI(String.format("%s/%s", webstoreAPILocation, resource));
    }

    public String sendHttpRequest(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}
