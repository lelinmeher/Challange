package com.dws.challenge.exception;

public class SelfTransferException extends RuntimeException {
    public SelfTransferException(String s) {
        super(s);
    }
}
