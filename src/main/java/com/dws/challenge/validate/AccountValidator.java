package com.dws.challenge.validate;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InvalidAccountDetailsException;
import com.dws.challenge.exception.InvalidTransferDetailsException;
import com.dws.challenge.exception.SelfTransferException;
import com.dws.challenge.model.TransferMoneyDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Component
public class AccountValidator {

    public void validate(TransferMoneyDetails transferMoneyDetails) throws InvalidTransferDetailsException {
        if (transferMoneyDetails.getAccountFromId() == null) {
            throw new InvalidTransferDetailsException("From account id required");
        }
        if (transferMoneyDetails.getAccountToId() == null) {
            throw new InvalidTransferDetailsException("To account id required");
        }
        if (transferMoneyDetails.getAmount() == null) {
            throw new InvalidTransferDetailsException("Amount id required");
        }
        if (transferMoneyDetails.getAccountToId().equals(transferMoneyDetails.getAccountFromId())) {
            throw new SelfTransferException("From accountId and To accountId can't be same");
        }
    }

    public void validateAccount(Account account) {
        if(account.getBalance() == null || account.getBalance().compareTo(BigDecimal.ZERO) < 0){
            throw new InvalidAccountDetailsException("Invalid Account Details");
        }

        if(StringUtils.isEmpty(account.getAccountId())){
            throw new InvalidAccountDetailsException("Invalid Account Details");
        }

    }
}
