package com.dws.challenge.exception;

public class InvalidDepositAmount extends RuntimeException {
    public InvalidDepositAmount(String message) {
        super(message);
    }
}
