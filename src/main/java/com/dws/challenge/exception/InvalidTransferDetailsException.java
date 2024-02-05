package com.dws.challenge.exception;

public class InvalidTransferDetailsException extends RuntimeException {
    public InvalidTransferDetailsException(String msg) {
        super(msg);
    }
}
