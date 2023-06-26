package com.apexon.compass.onboardservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.apexon.compass.onboardservice.constants.CommonConstants.IMAGE_FILE_EXT_CONTAIN_REGX;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NotNull
public class IconUploadDto {

    @Pattern(regexp = IMAGE_FILE_EXT_CONTAIN_REGX)
    private String file;

    private String fileName;

    private String fileType;

}