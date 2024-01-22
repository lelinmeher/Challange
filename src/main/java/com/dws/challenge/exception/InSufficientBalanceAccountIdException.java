package com.dws.challenge.exception;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class InSufficientBalanceAccountIdException extends Throwable {
    public InSufficientBalanceAccountIdException(String message) {
        super(message);
    }
}
