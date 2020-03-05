package net.donationstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ErrorDTO {

    @JsonProperty
    private Map<String, Object> error;
}
