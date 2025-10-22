package com.paymybuddy.paymybuddy.exception;

public class UsernameAlreadyExistsOnRegisterException extends RuntimeException {
    public UsernameAlreadyExistsOnRegisterException(String message) {
        super(message);
    }
}
