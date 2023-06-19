package com.apexon.compass.userservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties
public class RoleMapping {

    private Map<String, Set<String>> roles;

}
