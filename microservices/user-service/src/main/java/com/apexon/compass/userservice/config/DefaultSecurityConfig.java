package com.apexon.compass.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {

    // @formatter:off
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize ->
				authorize.anyRequest().authenticated()
			)
			.formLogin(withDefaults());
		return http.build();
	}
	// @formatter:on

    // @formatter:off
	@Bean
	UserDetailsService users() {
		UserDetails adminUser = User
				.withDefaultPasswordEncoder()
				.username("admin@apexon.com")
				.password("password")
				.roles("admin")
				.build();

        UserDetails manager = User
				.withDefaultPasswordEncoder()
				.username("manager@apexon.com")
				.password("password")
				.roles("manager")
				.build();

		return new InMemoryUserDetailsManager(adminUser, manager);
	}
	// @formatter:on

    // @formatter:off
    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(RoleMapping roleMapping) {
        return context -> {
            if(context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {

                // Spring stores roles as ROLE_<role name>. Below will extract the role name to look up widgets from map
                List<String> assignedRoles = context
                        .getPrincipal()
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(role -> role.split("ROLE_")[1])
                        .toList();

                List<String> widgets = assignedRoles
                        .stream()
                        .flatMap(role -> roleMapping.getRoles().getOrDefault(role, Set.of()).stream())
                        .toList();

                // TODO @Dev remove this eventually. This is currently for backward compatibility
                context
                        .getClaims()
                        .claim("rol", String.join(",", assignedRoles))
                        .claim("wdgt", String.join(",", widgets))
                        .claim("usr", context.getPrincipal().getName());
            }
        };
    }
    // @formatter:on

}
