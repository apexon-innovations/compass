package com.apexon.compass.psrservice.service;

import com.apexon.compass.exception.custom.RecordNotFoundException;
import com.apexon.compass.exception.custom.UnauthorizedException;
import com.apexon.compass.psrservice.dto.NpsDataResponseDto;
import com.apexon.compass.psrservice.dto.NpsReportsDto;
import com.apexon.compass.psrservice.dto.OnboardingProjectResponseDto;
import com.apexon.compass.psrservice.dto.IconDto;
import com.apexon.compass.psrservice.dto.IconUploadDto;
import com.apexon.compass.psrservice.dto.NpsDataRequestDto;
import com.apexon.compass.psrservice.dto.OffboardingProjectResponseDto;
import com.apexon.compass.psrservice.dto.OnboardingProjectRequestDto;

import java.text.ParseException;
import javax.naming.ServiceUnavailableException;

public interface IscService {

    OnboardingProjectResponseDto onboardProject(OnboardingProjectRequestDto onboardingProjectRequestDto)
            throws ServiceUnavailableException;

    OffboardingProjectResponseDto offboardProject(String iscProjectId);

    NpsDataResponseDto storeNpsData(String id, NpsDataRequestDto npsDataRequestDto)
            throws UnauthorizedException, ParseException;

    NpsReportsDto getNpsReport(String id, String reportType) throws UnauthorizedException, ParseException;

    IconDto updateIcon(String id, IconUploadDto iconUploadDto) throws RecordNotFoundException;

}
