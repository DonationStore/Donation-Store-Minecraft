package net.donationstore.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.donationstore.models.response.PackageResponse;

public class Command {
    public int id;

    public String command;

    public String uuid;

    @JsonProperty("require_online")
    public boolean requireOnline;

    public String server;

    public String amount;

    public String username;

    @JsonProperty("package")
    public PackageResponse packageResponse;
}
