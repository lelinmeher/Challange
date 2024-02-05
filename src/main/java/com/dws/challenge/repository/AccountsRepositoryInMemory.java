package com.dws.challenge.repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.exception.InSufficientBalanceAccountIdException;
import com.dws.challenge.exception.InvalidDepositAmount;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    private final Lock lock = new ReentrantLock();

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

    /*
    * ReentrantLock is more aptimal to use if we need to implement a thread that traverses a collecction,
    * locking the next node and then unlocking the current node.
    * It has validation of amount shount not have less than From account balance and be a positive number
    * */
    @Override
    public void transfer(Account accountFrom, Account accountTo, BigDecimal amount) throws InSufficientBalanceAccountIdException {

        lock.lock();
        try {
            if(accountFrom.getBalance().compareTo(amount)>=0){
                accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
                deposit(accountTo,amount);

                accounts.put(accountFrom.getAccountId(),accountFrom);
                accounts.put(accountTo.getAccountId(),accountTo);
                System.out.println("Transfer successful. New balance in Account " + accountFrom.getAccountId() +
                        ": " + accountFrom.getBalance() + ", New balance in Account " + accountTo.getAccountId() +
                        ": " + accountTo.getBalance());

            } else {
                throw new InSufficientBalanceAccountIdException(
                        "Account id " + accountFrom.getAccountId() + " has InSufficient Balance ");
            }
        }
        finally {
            lock.unlock();
        }
    }

    public void deposit(Account accountTo,BigDecimal amount) throws InvalidDepositAmount {
        if (amount == null || BigDecimal.ZERO.compareTo(amount) < 0) {
            lock.lock();
            try {
                accountTo.setBalance(accountTo.getBalance().add(amount));
            } finally {
                lock.unlock();
            }
        } else {
            throw new InvalidDepositAmount("Invalid deposit amount. Amount should be a positive number.");
        }
    }

}
