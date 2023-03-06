package com.github.keler1024.expensesandincomeservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.keler1024.expensesandincomeservice.security.exception.JWTClaimMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthUtils {
    @Value("${jwt_secret}")
    private String secret;

    public static boolean isValidBearerAuthHeader(String authorizationHeader) {
        return authorizationHeader != null && !authorizationHeader.isBlank()
                && authorizationHeader.startsWith("Bearer ") && authorizationHeader.length() > 7;
    }

    public static DecodedJWT decodeJWTFromHeader(String authorizationHeader) {
        return JWT.decode(authorizationHeader.substring(7));
    }
    public static Long getUserId(DecodedJWT token) throws JWTClaimMissingException {
        Claim userIdClaim = token.getClaim("userId");
        if (userIdClaim.isMissing() || userIdClaim.isNull()) {
            throw new JWTClaimMissingException("userId claim is missing or null");
        }
        return userIdClaim.asLong();
    }

    public static Long getUserIdFromAuthToken(String token) throws JWTClaimMissingException {
        DecodedJWT decodedToken = AuthUtils.decodeJWTFromHeader(token);
        Long ownerId;
        ownerId = AuthUtils.getUserId(decodedToken);
        return ownerId;
    }

    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("Authentication service of Expenses and Income application")
                .withClaimPresence("userId")
                .withClaimPresence("email")
                .build();
        return verifier.verify(token);
    }
}
