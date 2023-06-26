package com.apexon.compass.psrservice.service;

import com.apexon.compass.psrservice.dto.FeatureStagesDto;
import com.apexon.compass.psrservice.dto.SprintProgressDto;
import com.apexon.compass.psrservice.dto.DeliveredAcceptedDto;

public interface StoriesService {

    SprintProgressDto getCurrentSprintProgress(String projectId);

    FeatureStagesDto getFeaturesAtDiffrentStage(String projectId);

    DeliveredAcceptedDto getAcceptedDeliveredFeatures(String projectId);

}
