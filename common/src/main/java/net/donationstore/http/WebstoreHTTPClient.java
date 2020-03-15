package net.donationstore.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.donationstore.enums.HttpMethod;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.models.response.GatewayResponse;
import net.donationstore.exception.ClientException;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.donationstore.enums.HttpMethod.POST;

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
    public <T> GatewayResponse<T> sendRequest(GatewayRequest request, Class<T> responseClass) {

        GatewayResponse<T> gatewayResponse = new GatewayResponse<>();

        try {
            String requestBody = objectMapper.writeValueAsString(request.getBody());

            HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                    .uri(request.getUri());
            if (request.getMethod() == POST) {
                httpRequestBuilder
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody));
            }
            for (Map.Entry<String, String> headerEntry : request.getHeaders().entrySet()) {
                httpRequestBuilder.header(headerEntry.getKey(), headerEntry.getValue());
            }
            HttpRequest httpRequest = httpRequestBuilder.build();

            gatewayResponse.setBody(objectMapper.readValue(sendHttpRequest(httpClient, httpRequest), responseClass));
        } catch (InterruptedException | IOException exception) {
            logs.add("Exception when contacting the webstore API");
            logs.add(ExceptionUtils.getStackTrace(exception));
            throw new ClientException("Exception when contacting the webstore API", exception);
        }

        return gatewayResponse;
    }

    public String sendHttpRequest(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Secret-Key", secretKey);
        return headers;
    }

    public GatewayRequest buildDefaultRequest(String resourceUrl, HttpMethod method, Object body) throws URISyntaxException {
        GatewayRequest request = new GatewayRequest();
        request.setUri(String.format("%s/%s", webstoreAPILocation, resourceUrl));
        request.setMethod(method);
        request.setHeaders(getDefaultHeaders());
        return request;
    }
}
