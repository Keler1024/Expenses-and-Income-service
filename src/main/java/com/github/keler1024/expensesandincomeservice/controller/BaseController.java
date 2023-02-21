package com.github.keler1024.expensesandincomeservice.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.keler1024.expensesandincomeservice.security.AuthenticationUtils;
import com.github.keler1024.expensesandincomeservice.security.exception.JWTClaimMissingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BaseController {
    protected static Long getUserIdFromAuthToken(String token) {
        DecodedJWT decodedToken = AuthenticationUtils.decodeJWTFromHeader(token);
        Long ownerId;
        try {
            ownerId = AuthenticationUtils.getUserId(decodedToken);
        } catch (JWTClaimMissingException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "JWT userId claim is missing or doesn't have a value"
            );
        }
        return ownerId;
    }
}
