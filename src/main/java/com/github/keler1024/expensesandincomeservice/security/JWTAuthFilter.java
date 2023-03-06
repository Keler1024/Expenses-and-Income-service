package com.github.keler1024.expensesandincomeservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.keler1024.expensesandincomeservice.security.exception.JWTClaimMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Temporary implementation!!!

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private final AuthUtils authUtils;

    @Autowired
    public JWTAuthFilter(AuthUtils authUtils) {
        this.authUtils = authUtils;
    }
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (!AuthUtils.isValidBearerAuthHeader(authHeader)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header");
            return;
        }
        String jwt = authHeader.substring(7);
        if(jwt.isBlank()){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No JWT Token in Bearer Header");
            return;
        }
        Long userId;
        try {
            DecodedJWT decodedJWT = authUtils.verifyToken(jwt);
            userId = AuthUtils.getUserId(decodedJWT);
        } catch (JWTVerificationException | JWTClaimMissingException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        AuthContext.setUserId(userId);
        filterChain.doFilter(request, response);
    }
}
