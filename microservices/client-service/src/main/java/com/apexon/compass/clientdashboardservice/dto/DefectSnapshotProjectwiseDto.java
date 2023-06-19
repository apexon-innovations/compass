package com.apexon.compass.clientdashboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectSnapshotProjectwiseDto {

    private Integer projectId;

    private String projectName;

    private Integer totalDefects;

    private PlannedBacklogOpenDefectsDto plannedOpen;

    private PlannedBacklogOpenDefectsDto backlogOpen;

}
