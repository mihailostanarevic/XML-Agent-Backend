package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdSearchResponse {

    private UUID adID;          //

    private boolean limitedDistance;

    private int seats;

    private boolean cdw;

    private LocalDate creationDate;

    private List<PhotoSearchResponse> photos;       //
}
