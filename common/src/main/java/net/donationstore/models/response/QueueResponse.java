package net.donationstore.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.donationstore.models.response.PaymentsResponse;

import java.util.ArrayList;

public class QueueResponse {

    @JsonProperty("payments")
    public ArrayList<PaymentsResponse> payments;

    public QueueResponse() {
        payments = new ArrayList<>();
    }
}
