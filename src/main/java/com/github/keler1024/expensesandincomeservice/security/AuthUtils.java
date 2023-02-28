package com.github.keler1024.expensesandincomeservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.keler1024.expensesandincomeservice.security.exception.JWTClaimMissingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthUtils {
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

    public static Long getUserIdFromAuthToken(String token) {
        DecodedJWT decodedToken = AuthUtils.decodeJWTFromHeader(token);
        Long ownerId;
        try {
            ownerId = AuthUtils.getUserId(decodedToken);
        } catch (JWTClaimMissingException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "JWT userId claim is missing or doesn't have a value"
            );
        }
        return ownerId;
    }
}
