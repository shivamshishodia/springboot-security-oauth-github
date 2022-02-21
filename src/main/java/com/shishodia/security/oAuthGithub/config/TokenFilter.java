package com.shishodia.security.oAuthGithub.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenStore tokenStore;

    public TokenFilter(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /** TokenFilter checks the authorization token in the header. 
     * If the authorization token is found, it spilts the 'Bearer' part away.
     * After this we check if the token is present in the Token Store. 
     * If yes, we place it inside the security context.
    */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // The filter picks the authorization token from header. 
        String authToken = request.getHeader("Authorization");
        if (authToken != null) {
            // If the authorization token is found, it spilts the 'Bearer' part away.
            String token = authToken.split(" ")[1];
            // After this we check if the token is present in the TokenStore. 
            Authentication authentication = tokenStore.getAuth(token);
            if (authentication != null) {
                // If yes, we place it inside the security context.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
    
}
