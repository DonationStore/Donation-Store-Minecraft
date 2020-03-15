package net.donationstore.models.response;

import net.donationstore.models.Command;
import net.donationstore.models.Meta;
import net.donationstore.models.Variable;

import java.util.ArrayList;

public class PaymentsResponse {
    // ID -> Command
    public Meta meta;
    public ArrayList<Command> commands;
    public ArrayList<Variable> variables;

    public PaymentsResponse() {
        commands = new ArrayList<>();
        meta = new Meta();
        variables = new ArrayList<>();
    }
}
