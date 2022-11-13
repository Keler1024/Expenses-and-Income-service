package com.github.keler1024.expensesandincomeservice.exception;

public class NullArgumentServiceException extends RuntimeException {
    public NullArgumentServiceException() {
        super();
    }

    public NullArgumentServiceException(String message) {
        super(message);
    }

    public NullArgumentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullArgumentServiceException(Throwable cause) {
        super(cause);
    }
}
