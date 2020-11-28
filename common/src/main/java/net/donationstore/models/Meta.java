package net.donationstore.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Meta {
    @JsonProperty("payment_id")
    public String paymentId;

    public String user;

    public String uuid;

    @JsonProperty("transaction_id")
    public String transactionId;

    public String currency;

    public String email;
}
