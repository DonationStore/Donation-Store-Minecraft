package net.donationstore.license;

public class License {

    private String key;

    public License(String licenseKey) {
        this.key = licenseKey;
    }

    public String getKey() {
        return this.key;
    }

    public String toRequestBody() {
        return String.format("{ 'key': '%s' }", this.key);
    }
}
