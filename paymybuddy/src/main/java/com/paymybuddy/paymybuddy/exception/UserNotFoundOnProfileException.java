package com.paymybuddy.paymybuddy.exception;

public class UserNotFoundOnProfileException extends RuntimeException {
    public UserNotFoundOnProfileException(String message) {
        super(message);
    }
}
