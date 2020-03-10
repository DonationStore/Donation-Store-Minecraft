package net.donationstore.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.enums.CommandType;
import net.donationstore.enums.HttpMethod;
import net.donationstore.http.WebstoreHTTPClient;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApiCommand<T> extends AbstractCommand {

    @JsonIgnore
    private WebstoreHTTPClient webstoreHTTPClient;

    @JsonIgnore
    private String webstoreApiLocation;

    @JsonIgnore
    private String secretKey;

    public AbstractApiCommand() {
        super();
        webstoreHTTPClient = new WebstoreHTTPClient();
    }

    public abstract String getSupportedCommand();

    public abstract Command validate(String[] args);

    public abstract CommandType commandType();

    public abstract ArrayList<String> runCommand() throws Exception;

    public abstract String helpInfo();

    public String getInvalidCommandMessage() {
        return "Invalid usage of command. Help Info: ";
    }

    public String getWebstoreApiLocation() {
        return webstoreApiLocation;
    }

    public void setWebstoreApiLocation(String webstoreApiLocation) {
        this.webstoreApiLocation = webstoreApiLocation;
    }

    public WebstoreHTTPClient getWebstoreHTTPClient() {
        return webstoreHTTPClient;
    }

    public void setWebstoreHTTPClient(WebstoreHTTPClient webstoreHTTPClient) {
        this.webstoreHTTPClient = webstoreHTTPClient;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("secret-key", secretKey);
        return headers;
    }

    public GatewayRequest buildDefaultRequest(String resourceUrl, HttpMethod method, Object body) throws URISyntaxException {
        GatewayRequest request = new GatewayRequest();
        request.setUri(String.format("%s/%s", getWebstoreApiLocation(), resourceUrl));
        request.setMethod(method);
        request.setHeaders(getDefaultHeaders());
        return request;
    }
}
