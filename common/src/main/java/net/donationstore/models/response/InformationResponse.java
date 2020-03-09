package net.donationstore.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class InformationResponse {
    @JsonProperty("webstore")
    public Map<String, Object> webstore;

    @JsonProperty("server")
    public Map<String, Object> server;
}
