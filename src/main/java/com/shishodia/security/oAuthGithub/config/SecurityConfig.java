package com.shishodia.security.oAuthGithub.config;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // This object mapper will be used to write JSON results.
    private final ObjectMapper mapper;

    // Responsible for generating token using a random UUID (universal unique identifier).
    private final TokenStore tokenStore; 
    private final TokenFilter tokenFilter;

    public SecurityConfig(ObjectMapper mapper, TokenStore tokenStore, TokenFilter tokenFilter) {
        this.mapper = mapper;
        this.tokenStore = tokenStore;
        this.tokenFilter = tokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors() /** Refer corsConfigurationSource method below. */ 
            .and()
            .authorizeRequests()
            /** Allow access to oAuth and login URLs without the need of authentication. */
            .antMatchers("/oauth2/**", "/login**").permitAll()
            /** Any other request to the URIs should be authenticated. */
            .anyRequest().authenticated()
            .and()
            .oauth2Login().authorizationEndpoint()
            /** Use HashMap to hold the OAuthRequest in-memmory */
            .authorizationRequestRepository(new InMemoryRequestRepository())
            .and()
            /** Upon success, the auth object will be converted to JWT token. */
            .successHandler(this::successHandler)
            .and()
            /** Unauthenticated request will be shown 401 unauthorized error. */
            .exceptionHandling()
            .authenticationEntryPoint(this::authenticationEntryPoint);
        
        // Add tokenFilter before UsernamePasswordAuthenticationFilter.
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /** Generate the token using the TokenStore call and return the token in the response to clients on auth success. */
    private void successHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws JsonProcessingException, IOException {
        String token = tokenStore.generateToken(authentication);
        response.getWriter().write(
            mapper.writeValueAsString(Collections.singletonMap("accessToken", token))
        );
    }

    /** Unauthenticated request will be shown 401 unauthorized error. */
    private void authenticationEntryPoint(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws JsonProcessingException, IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(Collections.singletonMap("error", "Unauthenticated.")));
    }

    /** Allow XHR requests. */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
}