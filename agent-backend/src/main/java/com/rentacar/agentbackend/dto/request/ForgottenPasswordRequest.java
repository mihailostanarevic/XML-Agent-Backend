package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class ForgottenPasswordRequest {

    private String username;

    private String favoriteSportsClub;

    private String theBestChildhoodFriendsName;
}
