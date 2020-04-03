package net.donationstore.models.request;

import net.donationstore.enums.HttpMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class GatewayRequest {

    private URI uri;
    private Object body;
    private HttpMethod method;
    private Map<String, String> headers;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setUri(String uri) throws URISyntaxException {
        this.uri = new URI(uri);
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}