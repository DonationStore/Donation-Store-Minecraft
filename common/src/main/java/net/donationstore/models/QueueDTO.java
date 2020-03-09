package net.donationstore.models;

import java.util.ArrayList;

public class QueueDTO {

    public ArrayList<CommandExectionPayloadDTO> commandExectionPayloadDTO;

    public QueueDTO() {
        commandExectionPayloadDTO = new ArrayList<>();
    }
}
