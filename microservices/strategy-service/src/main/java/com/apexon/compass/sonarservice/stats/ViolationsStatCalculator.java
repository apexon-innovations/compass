package com.apexon.compass.sonarservice.stats;

import com.apexon.compass.sonarservice.dto.NewViolationsDto;
import com.apexon.compass.sonarservice.dto.SearchParametersDto;
import com.apexon.compass.sonarservice.dto.ViolationsDto;
import com.apexon.compass.sonarservice.repository.SonarStatsRepository;
import com.apexon.compass.entities.Project;
import com.apexon.compass.entities.NewViolations;
import com.apexon.compass.entities.SonarStats;
import com.apexon.compass.entities.Violations;
import com.apexon.compass.exception.custom.RecordNotFoundException;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ViolationsStatCalculator implements StatCalculator {

    private SonarStatsRepository sonarRepository;

    @Override
    public SearchParametersDto calculate(Project project) {
        List<SonarStats> sonarStatsUpdated = new ArrayList<>();
        List<Violations> violations = new ArrayList<>();
        List<NewViolations> newViolations = new ArrayList<>();

        project.getSonar().forEach(sonar -> {
            List<SonarStats> sonarStat = sonarRepository.findBySonarProjectId(sonar.getKey(),
                    Sort.by(Sort.Direction.DESC, "createdDate"));
            if (!sonarStat.isEmpty()) {
                sonarStatsUpdated.add(sonarStat.get(0));
                if (sonarStat.get(0).getViolations() != null) {
                    violations.add(sonarStat.get(0).getViolations());
                }
                if (sonarStat.get(0).getNewViolations() != null) {
                    newViolations.add(sonarStat.get(0).getNewViolations());
                }
            }
        });
        if (sonarStatsUpdated.isEmpty()) {
            throw new RecordNotFoundException("No Record found");
        }

        SearchParametersDto serachParameterDto = getAccumlatedDataOfNewViolations(newViolations);

        getAccumlatedDataOfViolations(violations, serachParameterDto);

        if (serachParameterDto.getNewViolations().getBlocker() == null
                && serachParameterDto.getNewViolations().getCritical() == null
                && serachParameterDto.getNewViolations().getInfo() == null
                && serachParameterDto.getNewViolations().getMajor() == null
                && serachParameterDto.getNewViolations().getMinor() == null
                && serachParameterDto.getNewViolations().getTotal() == null) {
            serachParameterDto.setNewViolations(null);
        }
        if (serachParameterDto.getViolations().getBlocker() == null
                && serachParameterDto.getViolations().getCritical() == null
                && serachParameterDto.getViolations().getInfo() == null
                && serachParameterDto.getViolations().getMajor() == null
                && serachParameterDto.getViolations().getMinor() == null
                && serachParameterDto.getViolations().getTotal() == null) {
            serachParameterDto.setViolations(null);
        }

        serachParameterDto.setId(project.getId());
        serachParameterDto.setName(project.getName());
        return serachParameterDto;
    }

    private SearchParametersDto getAccumlatedDataOfViolations(List<Violations> violationsList,
            SearchParametersDto serachParameterDto) {
        if (!violationsList.isEmpty()) {
            serachParameterDto.setViolations(ViolationsDto.builder()
                .blocker(violationsList.stream().noneMatch(a -> a.getBlocker() != null) ? null
                        : violationsList.stream()
                            .filter(a -> a.getBlocker() != null)
                            .mapToInt(Violations::getBlocker)
                            .sum())
                .major(violationsList.stream().noneMatch(a -> a.getMajor() != null) ? null
                        : violationsList.stream()
                            .filter(a -> a.getMajor() != null)
                            .mapToInt(Violations::getMajor)
                            .sum())
                .critical(violationsList.stream().noneMatch(a -> a.getCritical() != null) ? null
                        : violationsList.stream()
                            .filter(a -> a.getCritical() != null)
                            .mapToInt(Violations::getCritical)
                            .sum())
                .info(violationsList.stream().noneMatch(a -> a.getInfo() != null) ? null
                        : violationsList.stream().filter(a -> a.getInfo() != null).mapToInt(Violations::getInfo).sum())
                .minor(violationsList.stream().noneMatch(a -> a.getMinor() != null) ? null
                        : violationsList.stream()
                            .filter(a -> a.getMinor() != null)
                            .mapToInt(Violations::getMinor)
                            .sum())
                .total(violationsList.stream().noneMatch(a -> a.getTotal() != null) ? null
                        : violationsList.stream()
                            .filter(a -> a.getTotal() != null)
                            .mapToInt(Violations::getTotal)
                            .sum())
                .build());

            return serachParameterDto;
        }
        return serachParameterDto;
    }

    public SearchParametersDto getAccumlatedDataOfNewViolations(List<NewViolations> newViolationsList) {

        if (!newViolationsList.isEmpty()) {
            return SearchParametersDto.builder()
                .newViolations(NewViolationsDto.builder()
                    .blocker(newViolationsList.stream().noneMatch(a -> a.getBlocker() != null) ? null
                            : newViolationsList.stream()
                                .filter(a -> a.getBlocker() != null)
                                .mapToInt(NewViolations::getBlocker)
                                .sum())
                    .major(newViolationsList.stream().noneMatch(a -> a.getMajor() != null) ? null
                            : newViolationsList.stream()
                                .filter(a -> a.getMajor() != null)
                                .mapToInt(NewViolations::getMajor)
                                .sum())
                    .critical(newViolationsList.stream().noneMatch(a -> a.getCritical() != null) ? null
                            : newViolationsList.stream()
                                .filter(a -> a.getCritical() != null)
                                .mapToInt(NewViolations::getCritical)
                                .sum())
                    .info(newViolationsList.stream().noneMatch(a -> a.getInfo() != null) ? null
                            : newViolationsList.stream()
                                .filter(a -> a.getInfo() != null)
                                .mapToInt(NewViolations::getInfo)
                                .sum())
                    .minor(newViolationsList.stream().noneMatch(a -> a.getMinor() != null) ? null
                            : newViolationsList.stream()
                                .filter(a -> a.getMinor() != null)
                                .mapToInt(NewViolations::getMinor)
                                .sum())
                    .total(newViolationsList.stream().noneMatch(a -> a.getTotal() != null) ? null
                            : newViolationsList.stream()
                                .filter(a -> a.getTotal() != null)
                                .mapToInt(NewViolations::getTotal)
                                .sum())
                    .build())
                .build();
        }
        return SearchParametersDto.builder().build();
    }

}
