package com.apexon.compass.psrservice.service.impl;

import static com.apexon.compass.constants.PsrServiceConstants.MORE_THAN;
import static com.apexon.compass.constants.PsrServiceConstants.NA;
import static com.apexon.compass.constants.PsrServiceConstants.NULL;
import static com.apexon.compass.constants.PsrServiceConstants.RANGEARRAY;

import com.apexon.compass.entities.Project;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.ServiceException;
import com.apexon.compass.exception.custom.TypeConversionException;
import com.apexon.compass.psrservice.dto.CriteriawiseCoeDto;
import com.apexon.compass.psrservice.dto.TeamwiseCOEDto;
import com.apexon.compass.psrservice.repository.ProjectRepository;
import com.apexon.compass.psrservice.service.CoeService;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__({ @Autowired }))
public class CoeServiceImpl implements CoeService {

    private ProjectRepository projectRepository;

    @Override
    public List<TeamwiseCOEDto> getTeamSizeDetails() {
        try {
            List<TeamwiseCOEDto> response = new ArrayList<>();
            List<Project> projectDetails = projectRepository.findByIsDeletedNullOrIsDeletedFalse();
            if (projectDetails.isEmpty()) {
                throw new RecordNotFoundException("Error in finding all project details");
            }
            List<Integer> rangeList = Arrays.asList(RANGEARRAY);
            int prevRange = 0;
            for (int i = 0; i < rangeList.size(); i++) {
                int range = rangeList.get(i);
                range = range + prevRange;
                final int prevFinal = prevRange;
                final int rangeFinal = range;
                String mapKey = StringUtils.join(prevRange + 1, "-", range);
                if (i == rangeList.size() - 1) {
                    mapKey = MORE_THAN + prevFinal;
                }
                Supplier<Stream<Project>> projectStream = () -> projectDetails.stream()
                    .filter(projectDetail -> CollectionUtils.isNotEmpty(projectDetail.getResources()))
                    .filter(project -> checkBetween(project.getResources().get(0).getActual(), prevFinal + 1,
                            rangeFinal));
                int projectResources = BigDecimal
                    .valueOf(projectStream.get()
                        .map(project -> project.getResources().get(0).getActual())
                        .reduce(0.0, Double::sum))
                    .setScale(0, RoundingMode.HALF_UP)
                    .intValue();
                TeamwiseCOEDto.builder()
                    .teamSize(mapKey)
                    .resources(projectResources)
                    .projectCount((int) projectStream.get().count())
                    .build();
                prevRange = range;
                response.add(TeamwiseCOEDto.builder()
                    .teamSize(mapKey)
                    .resources(projectResources)
                    .projectCount((int) projectStream.get().count())
                    .build());
            }
            return response;
        }
        catch (NumberFormatException ex) {
            throw new TypeConversionException("Type conversion error " + ex.getMessage());
        }
    }

    @Override
    public List<CriteriawiseCoeDto> getCoeCount(String propertyName) {
        List<CriteriawiseCoeDto> response = new ArrayList<>();
        List<Project> projectDetails = projectRepository.findByIsDeletedNullOrIsDeletedFalse();
        if (projectDetails.isEmpty()) {
            throw new RecordNotFoundException("No project data available");
        }
        Map<String, Long> countMap = projectDetails.stream()
            .collect(Collectors.groupingBy(getFunction(propertyName), Collectors.counting()));
        for (Map.Entry<String, Long> mapEntry : countMap.entrySet()) {
            String industry;
            industry = NULL.equalsIgnoreCase(mapEntry.getKey()) ? NA : mapEntry.getKey();
            response.add(CriteriawiseCoeDto.builder().key(industry).value(mapEntry.getValue().intValue()).build());
        }
        return response;
    }

    private Function<Project, String> getFunction(String propertyName) {
        return iscProject -> {
            try {
                return PropertyUtils.getProperty(iscProject, propertyName) + StringUtils.EMPTY;
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                throw new ServiceException("Exception in getFunction" + e.getMessage());
            }
        };
    }

    private boolean checkBetween(Double toCheck, int from, int to) {
        int toCheckInt = -1;
        if (toCheck != null) {
            toCheckInt = BigDecimal.valueOf(toCheck).setScale(0, RoundingMode.HALF_UP).intValue();
        }
        return Range.between(from, to).contains(toCheckInt);
    }

}
