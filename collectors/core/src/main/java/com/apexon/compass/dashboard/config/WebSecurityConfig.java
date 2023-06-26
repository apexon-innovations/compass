package com.apexon.compass.dashboard.config;

import com.apexon.compass.dashboard.auth.AuthenticationResultHandler;
import com.apexon.compass.dashboard.auth.apitoken.ApiTokenAuthenticationProvider;
import com.apexon.compass.dashboard.auth.apitoken.ApiTokenRequestFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties
public class WebSecurityConfig {

	@Bean
	public AuthenticationManager authManager(HttpSecurity http,
			ApiTokenAuthenticationProvider apiTokenAuthenticationProvider) throws Exception {

		AuthenticationManagerBuilder authenticationManagerBuilder = http
			.getSharedObject(AuthenticationManagerBuilder.class)
			.authenticationProvider(apiTokenAuthenticationProvider);
		return authenticationManagerBuilder.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager,
			AuthenticationResultHandler authenticationResultHandler) throws Exception {

		http.headers().cacheControl();

		http.csrf()
			.disable()
			.authorizeHttpRequests()
			.requestMatchers("/ping")
			.permitAll()
			.requestMatchers("/refresh")
			.permitAll()
			.requestMatchers("/swagger/**")
			.permitAll()
			.requestMatchers(HttpMethod.GET, "/v2/api-docs")
			.permitAll()
			.requestMatchers(HttpMethod.GET, "/**")
			.permitAll()

			.anyRequest()
			.authenticated()
			.and()
			.addFilterBefore(apiTokenRequestFilter(authenticationManager, authenticationResultHandler),
					UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

		return http.build();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*");
			}
		};
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	protected ApiTokenRequestFilter apiTokenRequestFilter(AuthenticationManager authenticationManager,
			AuthenticationResultHandler authenticationResultHandler) {
		return new ApiTokenRequestFilter("/**", authenticationManager, authenticationResultHandler);
	}

}
