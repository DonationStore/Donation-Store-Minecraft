package net.donationstore.dto;

import java.util.ArrayList;

public class QueueDTO implements WebstoreAPIResponseDTO {

    public ArrayList<CommandExectionPayloadDTO> commandExectionPayloadDTO;

    public QueueDTO() {
        commandExectionPayloadDTO = new ArrayList<>();
    }
}
