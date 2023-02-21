package com.github.keler1024.expensesandincomeservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;

public class JWTUtil {
//    @Value("${jwt_secret}")
//    private String secret;

    public static DecodedJWT decodeToken(String token) throws JWTVerificationException {
        return JWT.decode(token);
    }

    public static Long getUserIdClaim(DecodedJWT token) {
        return token.getClaim("userId").asLong();
    }
}
