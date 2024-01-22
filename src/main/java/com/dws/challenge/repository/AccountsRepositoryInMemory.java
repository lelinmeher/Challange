package com.dws.challenge.repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.exception.InSufficientBalanceAccountIdException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void createAccount(Account account) throws DuplicateAccountIdException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }

    @Override
    public void transfer(Account accountFrom, Account accountTo, BigDecimal amount) throws InSufficientBalanceAccountIdException {
        if(accountFrom.getBalance().compareTo(amount)>=0){
            accountTo.getBalance().add(amount);
            accountFrom.getBalance().subtract(amount);
        }else{
            throw new InSufficientBalanceAccountIdException(
                    "Account id " + accountFrom.getAccountId() + " has InSufficient Balance ");
        }

    }

}
