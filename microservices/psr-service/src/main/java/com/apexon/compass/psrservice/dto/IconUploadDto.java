package com.apexon.compass.psrservice.dto;

import static com.apexon.compass.constants.PsrServiceConstants.IMAGE_FILE_EXT_CONTAIN_REGX;
import static com.apexon.compass.constants.PsrServiceConstants.IMAGE_FILE_EXT_REGX;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NotNull
public class IconUploadDto {

    @Size(max = 255)
    @Pattern(regexp = IMAGE_FILE_EXT_REGX)
    private String fileName;

    @Pattern(regexp = IMAGE_FILE_EXT_CONTAIN_REGX)
    private String file;

}
