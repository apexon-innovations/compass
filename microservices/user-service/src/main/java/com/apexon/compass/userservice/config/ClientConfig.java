package com.apexon.compass.userservice.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "oauth2")
@Data
public class ClientConfig {

    private List<OAuth2Props> clients;

}
