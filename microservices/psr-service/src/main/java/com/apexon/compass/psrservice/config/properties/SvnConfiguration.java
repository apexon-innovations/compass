package com.apexon.compass.psrservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "des.svn")
public class SvnConfiguration {

    private String url;

    private String userName;

    private String password;

}
