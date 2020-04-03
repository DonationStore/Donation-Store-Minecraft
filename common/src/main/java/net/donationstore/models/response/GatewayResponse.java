package net.donationstore.models.response;

public class GatewayResponse<T> {

    private T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

}
