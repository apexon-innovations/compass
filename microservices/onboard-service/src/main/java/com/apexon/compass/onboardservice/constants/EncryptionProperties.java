package com.apexon.compass.onboardservice.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EncryptionProperties {

    @Value("${encryption.salt}")
    private String salt;

    @Value("${encryption.random}")
    private String random;

}
