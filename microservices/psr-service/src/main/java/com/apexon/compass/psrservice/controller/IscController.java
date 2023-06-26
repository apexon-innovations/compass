package com.apexon.compass.psrservice.controller;

import static com.apexon.compass.constants.PsrServiceConstants.ID_CAPITALIZED;
import static com.apexon.compass.constants.PsrServiceConstants.ISC_PROJECT_ID_CAPITALIZED;
import static com.apexon.compass.constants.PsrServiceConstants.PROJECT_ID_VALIDATION_MESSAGE;
import static com.apexon.compass.constants.PsrServiceConstants.PROJECT_ID_VALIDATION_REGEX;
import static com.apexon.compass.constants.PsrServiceConstants.RECENT;
import static com.apexon.compass.constants.PsrServiceConstants.X_USER_HEADER;
import static com.apexon.compass.constants.PsrServiceRouteConstants.*;
import static org.springframework.http.HttpStatus.CREATED;
import java.text.ParseException;
import javax.naming.ServiceUnavailableException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import com.apexon.compass.psrservice.dto.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.apexon.compass.exception.custom.ForbiddenException;
import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.exception.handler.ApiExceptionHandler;
import com.apexon.compass.psrservice.service.IscService;
import com.apexon.compass.psrservice.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@RestController
@RequestMapping(value = PROJECT)
@AllArgsConstructor
@Validated
public class IscController extends ApiExceptionHandler {

    private IscService iscService;

    private ProjectService projectService;

    @PostMapping(path = ON_BOARD)
    @ResponseStatus(value = CREATED)
    public OnboardingProjectResponseDto onboardProject(
            @Valid @RequestBody OnboardingProjectRequestDto onboardingProjectRequestDto)
            throws RecordNotFoundException, ServiceUnavailableException {
        return iscService.onboardProject(onboardingProjectRequestDto);
    }

    @PutMapping(path = PROJECTID_OFFBOARD)
    public OffboardingProjectResponseDto offboardProject(@Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
            message = PROJECT_ID_VALIDATION_MESSAGE) @PathVariable(ISC_PROJECT_ID_CAPITALIZED) String iscProjectId) {
        return iscService.offboardProject(iscProjectId);
    }

    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = PROJECT_ID)
    public ProjectDataDto getProject(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @PathVariable(ID_CAPITALIZED) String id)
            throws UnauthorizedException, RecordNotFoundException, ForbiddenException {
        return projectService.getProject(id);
    }

    @GetMapping(path = ALL_PROJECT)
    public AllProjectsDto getAssignedProjects(@RequestHeader(value = X_USER_HEADER) @Email String email)
            throws RecordNotFoundException {
        return projectService.getAssignedProjects(email);
    }

    @RolesAllowed({ "admin", "manager" })
    @PostMapping(path = NPS_DATAPARSING)
    @ResponseStatus(value = CREATED)
    public NpsDataResponseDto storeNpsData(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @PathVariable(ISC_PROJECT_ID_CAPITALIZED) String id,
            @Valid @RequestBody NpsDataRequestDto npsDataRequestDto) throws UnauthorizedException, ParseException {
        return iscService.storeNpsData(id, npsDataRequestDto);
    }

    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = NPS_DATAPARSING)
    public NpsReportsDto getNpsReport(@Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
            message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @PathVariable(ISC_PROJECT_ID_CAPITALIZED) String id,
            @RequestParam(required = false, defaultValue = RECENT) String reportType)
            throws UnauthorizedException, RecordNotFoundException, ForbiddenException, ParseException {
        return iscService.getNpsReport(id, reportType);
    }

    @PutMapping(path = UPDATE_PROJECT_ICON)
    public IconDto updateIcon(
            @Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
                    message = PROJECT_ID_VALIDATION_MESSAGE) @PathVariable(ISC_PROJECT_ID_CAPITALIZED) String id,
            @Valid @RequestBody IconUploadDto iconUploadDtoDto) throws RecordNotFoundException {
        return iscService.updateIcon(id, iconUploadDtoDto);
    }

    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = PROJECT_COMPLIANCE)
    public ComplianceDto getProjectCompliance(@Pattern(regexp = PROJECT_ID_VALIDATION_REGEX,
            message = PROJECT_ID_VALIDATION_MESSAGE) @NonNull @PathVariable(ISC_PROJECT_ID_CAPITALIZED) String id) {
        return projectService.getProjectCompliance(id);
    }

    @RolesAllowed({ "admin", "manager" })
    @GetMapping(path = ALL_CLIENT_PROJECT)
    public AllClientProjectDto getAssignedProjectsClientPM(@RequestHeader(value = X_USER_HEADER) @Email String email)
            throws RecordNotFoundException {
        return projectService.getClientProjects(email);
    }

}
