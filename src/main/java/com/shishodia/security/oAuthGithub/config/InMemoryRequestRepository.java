package com.shishodia.security.oAuthGithub.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/** Holds the OAuth request temporarily in-memory while the Github request is being made. */
public class InMemoryRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final Map<String, OAuth2AuthorizationRequest> cache = new HashMap<>();

    /** Holds the OAuth request temporarily in-memory while the Github request is being made. */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        /** If the request contains 'state' parameter, remove it from the cache and return the removed cache value. */
        String state = request.getParameter("state");
        if (state != null) {
            // Check logic of this method below.
            return removeAuthorizationRequest(request);
        }
        return null;
    }

    // Removes the authorizationRequest.
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        String state = request.getParameter("state");
        if (state != null) {
            return cache.remove(state); /** If the request contains 'state' parameter, remove it from the cache and return the removed cache value. */
        }
        return cache.getOrDefault(request.getParameter("state"), null);        
    }

    // Stores the authorizationRequest.
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        String state = authorizationRequest.getState();
        cache.put(state, authorizationRequest);
    }

}
