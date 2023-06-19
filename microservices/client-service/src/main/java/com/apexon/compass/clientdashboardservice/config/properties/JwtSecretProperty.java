package com.apexon.compass.clientdashboardservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.apexon.compass.constants.PsrServiceConstants.JWT;

@Data
@Configuration
@ConfigurationProperties(prefix = JWT)
public class JwtSecretProperty {

    private String key;

}
