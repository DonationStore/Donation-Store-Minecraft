package net.donationstore.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class InformationResponse {
    @JsonProperty("webstore")
    private Map<String, Object> webstore;

    @JsonProperty("server")
    private Map<String, Object> server;

    public Map<String, Object> getWebstore() {
        return webstore;
    }

    public InformationResponse setWebstore(Map<String, Object> webstore) {
        this.webstore = webstore;
        return this;
    }

    public Map<String, Object> getServer() {
        return server;
    }

    public InformationResponse setServer(Map<String, Object> server) {
        this.server = server;
        return this;
    }
}
