package com.apexon.compass.sonarservice.stats;

import static com.apexon.compass.constants.StrategyServiceConstants.ADDED;
import static com.apexon.compass.constants.StrategyServiceConstants.REMOVED;

import com.apexon.compass.sonarservice.dto.SearchParametersDto;
import com.apexon.compass.sonarservice.repository.SonarStatsRepository;
import com.apexon.compass.entities.Project;
import com.apexon.compass.entities.SonarStats;
import com.apexon.compass.exception.custom.RecordNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class RiskStatCalculator implements StatCalculator {

    private SonarStatsRepository sonarRepository;

    @Override
    public SearchParametersDto calculate(Project project) {

        Map<String, Object> riskMap = new HashMap<>();
        project.getSonar().forEach(sonar -> {
            List<SonarStats> sonarStat = sonarRepository.findBySonarProjectId(sonar.getKey(),
                    Sort.by(Sort.Direction.DESC, "sonarUpdatedDate"));

            if (sonarStat.isEmpty()) {
                throw new RecordNotFoundException("Unable to find records for this project");
            }

            if (sonarStat.get(0).getViolations() != null && sonarStat.get(0).getViolations().getCritical() != null) {
                log.info(sonarStat.get(0).getViolations().getCritical().toString());
                if (!riskMap.containsKey(sonar.getLanguage())) {
                    riskMap.put(sonar.getLanguage(), sonarStat.get(0).getViolations().getCritical());
                }
                else {
                    riskMap.put(sonar.getLanguage(),
                            (int) riskMap.get(sonar.getLanguage()) + sonarStat.get(0).getViolations().getCritical());
                }

                List<SonarStats> sonarStatForUpdated = sonarRepository.findBySonarProjectId(sonar.getKey(),
                        PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "sonarUpdatedDate")));
                if (sonarStatForUpdated.isEmpty()) {
                    throw new RecordNotFoundException("Unable to find records for this project");
                }
                if (sonarStatForUpdated.size() < 2) {
                    riskMap.put(ADDED, 0);
                    riskMap.put(REMOVED, 0);
                }
                else {
                    riskMap.put(ADDED,
                            (int) riskMap.get(ADDED) + (sonarStatForUpdated.get(0).getViolations().getCritical()
                                    - sonarStatForUpdated.get(1).getViolations().getCritical()));
                    riskMap.put(REMOVED,
                            (int) riskMap.get(REMOVED) + (sonarStatForUpdated.get(1).getViolations().getCritical()
                                    - sonarStatForUpdated.get(0).getViolations().getCritical()));
                }
            }
        });
        if (!riskMap.isEmpty()) {
            return SearchParametersDto.builder().risk(riskMap).id(project.getId()).name(project.getName()).build();
        }
        return SearchParametersDto.builder().id(project.getId()).name(project.getName()).build();
    }

}
