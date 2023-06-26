package com.apexon.compass.onboardservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @NotBlank
    @Pattern(regexp = "\\d+", message = "Value must be numeric")
    private String jiraProjectId;

    @NotBlank
    private String clientName;

    private long endDate;

    @NotEmpty
    @Valid
    private List<ManagerDetails> dm;

    @NotEmpty
    @Valid
    private List<Resources> resources;

    @NotEmpty
    @Valid
    private List<ClientManager> clientPm;

    @NotBlank
    private String logoImage;

    private boolean isDeleted;

    @NotBlank
    private String billingType;

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @Valid
    @NotEmpty
    private List<ManagerDetails> pm;

    private long startDate;

}
