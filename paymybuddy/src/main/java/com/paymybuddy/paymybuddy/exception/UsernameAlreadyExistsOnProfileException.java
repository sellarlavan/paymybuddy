package com.paymybuddy.paymybuddy.exception;

public class UsernameAlreadyExistsOnProfileException extends RuntimeException {
    public UsernameAlreadyExistsOnProfileException(String message) {
        super(message);
    }
}
