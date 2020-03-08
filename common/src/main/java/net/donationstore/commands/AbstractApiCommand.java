package net.donationstore.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.donationstore.dto.WebstoreAPIResponseDTO;
import net.donationstore.http.WebstoreHTTPClient;

import java.net.http.HttpClient;
import java.util.ArrayList;

public abstract class AbstractApiCommand extends AbstractCommand {

    @JsonIgnore
    private WebstoreHTTPClient webstoreHTTPClient;

    @JsonIgnore
    private Class webstoreAPIResponseDTO;

    public AbstractApiCommand() {
        super();
        webstoreHTTPClient = new WebstoreHTTPClient();
    }

    public abstract String getSupportedCommand();

    public abstract Command validate(String[] args);

    public abstract CommandType commandType();

    public abstract ArrayList<String> runCommand() throws Exception;

    public abstract String helpInfo();

    public String getInvalidCommandMessage() {
        return "Invalid usage of command. Help Info: ";
    }

    public WebstoreHTTPClient getWebstoreHTTPClient() {
        return webstoreHTTPClient;
    }

    public Class getWebstoreAPIResponseDTO() {
        return webstoreAPIResponseDTO;
    }

    public void setWebstoreAPIResponseDTO(Class webstoreAPIResponseDTO) {
        this.webstoreAPIResponseDTO = webstoreAPIResponseDTO;
    }
}
