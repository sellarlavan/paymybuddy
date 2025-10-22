package com.paymybuddy.paymybuddy.exception;

public class EmailAlreadyExistsOnRegisterException extends RuntimeException {
    public EmailAlreadyExistsOnRegisterException(String message) {
        super(message);
    }
}