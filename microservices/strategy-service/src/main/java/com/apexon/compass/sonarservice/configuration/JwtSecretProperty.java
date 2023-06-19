package com.apexon.compass.sonarservice.configuration;

import static com.apexon.compass.constants.PsrServiceConstants.JWT;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = JWT)
public class JwtSecretProperty {

    private String key;

}
