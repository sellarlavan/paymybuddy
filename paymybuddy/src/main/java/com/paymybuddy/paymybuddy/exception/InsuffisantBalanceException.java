package com.paymybuddy.paymybuddy.exception;

public class InsuffisantBalanceException extends RuntimeException {
    public InsuffisantBalanceException(String message) {
        super(message);
    }
}
