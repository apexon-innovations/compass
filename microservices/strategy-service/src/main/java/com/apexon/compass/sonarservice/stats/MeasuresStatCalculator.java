package com.apexon.compass.sonarservice.stats;

import static com.apexon.compass.utilities.DateTimeUtils.convertMinutesToWorkingDays;

import com.apexon.compass.entities.Project;
import com.apexon.compass.sonarservice.dto.MeasuresDto;
import com.apexon.compass.sonarservice.dto.SearchParametersDto;
import com.apexon.compass.sonarservice.repository.SonarStatsRepository;
import com.apexon.compass.entities.Ratings;
import com.apexon.compass.entities.SonarStats;
import com.apexon.compass.exception.custom.RecordNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class MeasuresStatCalculator implements StatCalculator {

    private SonarStatsRepository sonarRepository;

    @Override
    public SearchParametersDto calculate(Project project) {

        if (project.getSonar().isEmpty()) {
            throw new RecordNotFoundException("No data available");
        }
        List<Ratings> ratings = new ArrayList<>();
        List<SonarStats> sonarStats = new ArrayList<>();
        project.getSonar().forEach(sonar -> {
            List<SonarStats> sonarStat = sonarRepository.findBySonarProjectId(sonar.getKey(),
                    Sort.by(Sort.Direction.DESC, "createdDate"));
            if (sonarStat.isEmpty()) {
                throw new RecordNotFoundException("No record avilable");
            }
            sonarStats.add(sonarStat.get(0));
            if (sonarStat.get(0).getRatings() != null) {
                ratings.add(sonarStat.get(0).getRatings());
            }
        });
        SearchParametersDto searchParametersDto = getIndividualMeasure(ratings);
        if (!sonarStats.isEmpty()) {
            searchParametersDto.getMeasures()
                .setTechnicalDebt(convertMinutesToWorkingDays(
                        sonarStats.stream().noneMatch(a -> a.getTechnicalDebtIndex() != null) ? null
                                : sonarStats.stream()
                                    .filter(a -> a.getTechnicalDebtIndex() != null)
                                    .mapToInt(SonarStats::getTechnicalDebtIndex)
                                    .sum()));
        }

        MeasuresDto measuresDto = searchParametersDto.getMeasures();
        if (measuresDto.getEfficiency() == null && measuresDto.getRobustness() == null
                && measuresDto.getSecurity() == null && measuresDto.getTechnicalDebt() == null) {
            searchParametersDto.setMeasures(null);
        }
        searchParametersDto.setId(project.getId());
        searchParametersDto.setName(project.getName());
        return searchParametersDto;
    }

    public SearchParametersDto getIndividualMeasure(List<Ratings> ratings) {

        if (CollectionUtils.isEmpty(ratings)) {
            return SearchParametersDto.builder().build();
        }
        return SearchParametersDto.builder()
            .measures(
                    MeasuresDto.builder()
                        .security(ratings.stream().noneMatch(a -> a.getSecurity() != null) ? null
                                : BigDecimal.valueOf(ratings.stream()
                                    .filter(a -> a.getSecurity() != null)
                                    .mapToDouble(Ratings::getSecurity)
                                    .average()
                                    .getAsDouble()).setScale(1, RoundingMode.HALF_UP).doubleValue())
                        .efficiency(ratings.stream().noneMatch(a -> a.getReliability() != null)
                                ? null
                                : BigDecimal
                                    .valueOf(ratings.stream()
                                        .filter(a -> a.getReliability() != null)
                                        .mapToDouble(Ratings::getReliability)
                                        .average()
                                        .getAsDouble())
                                    .setScale(1, RoundingMode.HALF_UP)
                                    .doubleValue())
                        .robustness(ratings.stream().noneMatch(a -> a.getMaintainability() != null)
                                ? null
                                : BigDecimal
                                    .valueOf(ratings.stream()
                                        .filter(a -> a.getMaintainability() != null)
                                        .mapToDouble(Ratings::getMaintainability)
                                        .average()
                                        .getAsDouble())
                                    .setScale(1, RoundingMode.HALF_UP)
                                    .doubleValue())
                        .build())
            .build();
    }

}
