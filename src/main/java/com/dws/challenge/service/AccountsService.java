package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InSufficientBalanceAccountIdException;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountsService {

    @Getter
    private final AccountsRepository accountsRepository;

    @Autowired
    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public void createAccount(Account account) {
        this.accountsRepository.createAccount(account);
    }

    public Account getAccount(String accountId) {
        return this.accountsRepository.getAccount(accountId);
    }

    public void transfer(String accountFromId, String accountToId, BigDecimal amount) throws InSufficientBalanceAccountIdException {
        Account accountFrom = accountsRepository.getAccount(accountFromId);
        Account accountTo = accountsRepository.getAccount(accountToId);

        if (accountFrom != null && accountTo != null) {
           accountsRepository.transfer(accountFrom, accountTo, amount);
        }

    }
}
