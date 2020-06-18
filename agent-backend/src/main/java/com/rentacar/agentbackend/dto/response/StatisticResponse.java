package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticResponse {

    private UUID adIdMostKilometersTraveled;

    private String brandNameMostKilometersTraveled;

    private String modelNameMostKilometersTraveled;

    private String mostKilometersTraveled;

    private UUID adIdLeastKilometersTraveled;

    private String brandNameLeastKilometersTraveled;

    private String modelNameLeastKilometersTraveled;

    private String leastKilometersTraveled;

    private UUID adIdMostCommented;

    private String brandNameMostCommented;

    private String modelNameMostCommented;

    private String mostCommentedAd;

    private UUID adIdLeastCommented;

    private String brandNameLeastCommented;

    private String modelNameLeastCommented;

    private String leastCommentedAd;

    private UUID adIdMostRated;

    private String brandNameMostRated;

    private String modelNameMostRated;

    private String mostRateddAd;

    private UUID adIdLeastRated;

    private String brandNameLeastRated;

    private String modelNameLeastRated;

    private String leastRatedAd;

    private String bestAvgRating;

    private UUID adIdBestRated;

    private String brandNameBestRated;

    private String modelNameBestRated;

    private String worstAvgRating;

    private UUID adIdWorstRated;

    private String brandNameWorstRated;

    private String modelNameWorstRated;
}
