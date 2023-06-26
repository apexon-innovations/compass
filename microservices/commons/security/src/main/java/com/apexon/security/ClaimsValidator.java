package com.apexon.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;

public class ClaimsValidator {

    // TODO Add all claims validation which are required
    public static OAuth2TokenValidator<Jwt> usernameValidator() {
        return new JwtClaimValidator<>("preferred_username", username -> StringUtils.isNotBlank((String) username));
    }

}
