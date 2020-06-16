package net.donationstore.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebstoreResponse {

    @JsonProperty("type")
    public String webstoreType;
}
