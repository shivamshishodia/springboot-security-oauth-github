package com.shishodia.security.oAuthGithub.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class TokenStore {

    private final Map<String, Authentication> cache = new HashMap<>();

    /** Responsible for generating token using a random UUID (universal unique identifier). */
    public String generateToken(Authentication authentication) {
        String token = UUID.randomUUID().toString();
        cache.put(token, authentication);
        return token;
    }

    /** Fetch the token available in the hashmap cache. */
    public Authentication getAuth(String token) {
        return cache.getOrDefault(token, null);
    }
    
}
