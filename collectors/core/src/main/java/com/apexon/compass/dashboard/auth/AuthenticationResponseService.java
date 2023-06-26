package com.apexon.compass.dashboard.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthenticationResponseService {

	void handle(HttpServletResponse response, Authentication authentication);

}
