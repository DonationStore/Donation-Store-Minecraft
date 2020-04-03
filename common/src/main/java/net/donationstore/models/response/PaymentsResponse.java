package net.donationstore.models.response;

import net.donationstore.models.Command;
import net.donationstore.models.Meta;
import net.donationstore.models.Variable;

import java.util.ArrayList;
import java.util.List;

public class PaymentsResponse {
    public Meta meta;
    public List<Command> commands;
    public List<Variable> variables;

    public PaymentsResponse() {
        commands = new ArrayList<>();
        meta = new Meta();
        variables = new ArrayList<>();
    }
}
