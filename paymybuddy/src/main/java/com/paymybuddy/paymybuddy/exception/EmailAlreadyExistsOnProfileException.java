package com.paymybuddy.paymybuddy.exception;

public class EmailAlreadyExistsOnProfileException extends RuntimeException {
    public EmailAlreadyExistsOnProfileException(String message) {
        super(message);
    }
}
