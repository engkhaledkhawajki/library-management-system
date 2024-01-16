package com.example.librarymanagementsystem.config;

import jakarta.servlet.Filter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
// At the application startup, spring security will try to look for a bean of type SecurityFilterChain
// This SecurityFilterChain is the bean responsible for configuring all the HTTP security in the application
public class SecurityConfiguration {

    //finals are automatically injected
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception{
        //Note: Because the filter is once per request, we don't want to store the session state
        //(the session should be stateless).
        http.csrf().disable();
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/api/patrons/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated());
        http.sessionManagement(
                session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
