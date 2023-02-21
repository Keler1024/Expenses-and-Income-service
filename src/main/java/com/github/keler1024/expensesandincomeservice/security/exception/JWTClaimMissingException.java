package com.github.keler1024.expensesandincomeservice.security.exception;

public class JWTClaimMissingException extends Exception {
    public JWTClaimMissingException() {
        super();
    }

    public JWTClaimMissingException(String message) {
        super(message);
    }

    public JWTClaimMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JWTClaimMissingException(Throwable cause) {
        super(cause);
    }
}
