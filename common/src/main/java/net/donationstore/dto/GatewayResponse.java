package net.donationstore.dto;

public class GatewayResponse {
    // Code field, to check if successful etc because this is returned back to the command

    private Object body;

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
