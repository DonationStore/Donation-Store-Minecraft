package net.donationstore.http;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.donationstore.enums.HttpMethod;
import net.donationstore.models.error.Error;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.models.response.GatewayResponse;
import net.donationstore.exception.ClientException;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.donationstore.enums.HttpMethod.POST;

public class WebstoreHTTPClient {

    private OkHttpClient httpClient;
    private String secretKey;
    private ArrayList<String> logs;
    public ObjectMapper objectMapper;
    private String webstoreAPILocation;

    public WebstoreHTTPClient() {
        httpClient = new OkHttpClient();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
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

    public <T> GatewayResponse<T> sendRequest(GatewayRequest request, Class<T> responseClass) {

        GatewayResponse<T> gatewayResponse = new GatewayResponse<>();

        try {
            String requestBody = objectMapper.writeValueAsString(request.getBody());

            Request.Builder httpRequestBuilder = new Request.Builder()
                    .url(request.getUri().toURL());

            if (request.getMethod() == POST) {
                httpRequestBuilder.post(RequestBody.create(requestBody, MediaType.get("application/json; charset=utf-8")));
            }
            for (Map.Entry<String, String> headerEntry : request.getHeaders().entrySet()) {
                httpRequestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
            Request httpRequest = httpRequestBuilder.build();

            gatewayResponse.setBody(objectMapper.readValue(sendHttpRequest(httpClient, httpRequest), responseClass));

        } catch (InterruptedException | IOException exception) {
            logs.add("Exception when contacting the webstore API");
            logs.add(ExceptionUtils.getStackTrace(exception));
            throw new ClientException("Exception when contacting the webstore API", exception);
        }

        return gatewayResponse;
    }

    public String sendHttpRequest(OkHttpClient client, Request request) throws IOException, InterruptedException {
        Response httpResponse = client.newCall(request).execute();

        if (httpResponse.code() != 200) {
            try {
                System.out.println("Trying to jackson the error");
                Error error = objectMapper.readValue(httpResponse.body().string(), Error.class);

                throw new ClientException(error.getErrorMessage().getMessage());
            } catch (JsonParseException exception) {
                throw new ClientException(httpResponse.body().string());
            }
        } else {
            return httpResponse.body().string();
        }
    }

    public Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Secret-Key", secretKey);
        return headers;
    }

    public GatewayRequest buildDefaultRequest(String resourceUrl, HttpMethod method, Object body) throws URISyntaxException {
        GatewayRequest request = new GatewayRequest();
        request.setUri(String.format("%s/%s", webstoreAPILocation, resourceUrl));
        request.setBody(body);
        request.setMethod(method);
        request.setHeaders(getDefaultHeaders());
        return request;
    }
}
