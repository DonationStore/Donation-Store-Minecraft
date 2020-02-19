package net.donationstore.license;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.donationstore.exception.InvalidLicenseException;
import net.donationstore.exception.NoLicenseKeyException;
import net.donationstore.exception.WebstoreAPIException;
import net.donationstore.util.FormUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LicenseHandler {

    private String licenseKey;
    private HttpClient httpClient;
    private ArrayList<String> logs;

    public LicenseHandler() {
        logs = new ArrayList<>();
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public ArrayList<String> authorise(String licenseKey) throws Exception {

        if(licenseKey.equals("")) {
            logs.add("The given license key is empty or hasn't been provided");
            throw new NoLicenseKeyException(logs);
        } else {
            Map<String, String> data = new HashMap<>();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.donationstore.net/license/authorise"))
                    .header("Authorization", String.format("Bearer %s", licenseKey))
                    .POST(FormUtil.ofFormData(data))
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    logs.add("Invalid license. Please enter a valid license or buy one at: https://donationstore.net");
                    logs.add("If you believe this is a mistake, contact us at: https://donationstore.net/support");
                    throw new InvalidLicenseException(logs);
                }
            } catch (IOException exception) {
                throw new IOException("IOException when trying to authorise your license.");
            } catch (InterruptedException exception) {
                throw new InterruptedException("InterruptedException when trying to authorise your license.");
            }
        }

        return logs;
    }
}
