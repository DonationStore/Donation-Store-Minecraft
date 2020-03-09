package net.donationstore.models.response;

public class GatewayResponse<T> {
    // Code field, to check if successful etc because this is returned back to the command

    private T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

}
