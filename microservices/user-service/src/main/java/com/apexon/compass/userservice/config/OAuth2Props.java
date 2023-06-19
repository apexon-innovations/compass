package com.apexon.compass.userservice.config;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Props {

    private String clientId;

    private String clientSecret;

    private List<String> grantTypes;

    private List<String> redirectUri;

    private List<String> scopes;

}
