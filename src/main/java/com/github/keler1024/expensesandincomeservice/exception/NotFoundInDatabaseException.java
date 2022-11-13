package com.github.keler1024.expensesandincomeservice.exception;

public class NotFoundInDatabaseException extends RuntimeException {
    public NotFoundInDatabaseException() {
        super();
    }

    public NotFoundInDatabaseException(String message) {
        super(message);
    }

    public NotFoundInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundInDatabaseException(Throwable cause) {
        super(cause);
    }
}
