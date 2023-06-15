package com.apexon.compass.entities;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusData {

    private Long date;

    private Integer remainingEfforts;

    private Integer completedEfforts;

    private Integer newlyAddedEfforts;

    private Integer reopenEfforts;

    private Integer completedTasks;

    private Integer remainingTasks;

    private Integer acceptedEfforts;

    private Integer acceptedTasks;

    private Integer toDoTasks;

    private Integer inProgressTasks;

    private Integer blockerTasks;

    private Integer newlyAddedTasks;

    private Integer reopenTasks;

    private List<Team> team;

}
