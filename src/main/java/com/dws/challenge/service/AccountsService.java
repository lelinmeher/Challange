package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InSufficientBalanceAccountIdException;
import com.dws.challenge.model.TransferCompletionDetails;
import com.dws.challenge.model.TransferMoneyDetails;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountsService {

    @Getter
    private final AccountsRepository accountsRepository;

    private final NotificationService notificationService;

    @Autowired
    public AccountsService(AccountsRepository accountsRepository, NotificationService notificationService) {
        this.accountsRepository = accountsRepository;
        this.notificationService = notificationService;
    }

    public void createAccount(Account account) {
        this.accountsRepository.createAccount(account);
    }

    public Account getAccount(String accountId) {
        return this.accountsRepository.getAccount(accountId);
    }

    /*
    * Method to transfer money between accounts
    * sent notification to and from accounts
    * */
    public TransferCompletionDetails transfer(TransferMoneyDetails transferMoneyDetails) throws InSufficientBalanceAccountIdException {
        Account accountFrom = accountsRepository.getAccount(transferMoneyDetails.getAccountFromId());
        Account accountTo = accountsRepository.getAccount(transferMoneyDetails.getAccountToId());

        /* construct the transferCompletionDetails outside
            of the synchronized blocks for minimalistic locking.
         */
        TransferCompletionDetails transferCompletionDetails = new TransferCompletionDetails();
        transferCompletionDetails.setFrom(accountFrom.getAccountId());
        transferCompletionDetails.setTo(accountTo.getAccountId());

        if (accountFrom != null && accountTo != null) {
           accountsRepository.transfer(accountFrom, accountTo, transferMoneyDetails.getAmount());
           transferCompletionDetails.setFromBalance(accountFrom.getBalance());
           transferCompletionDetails.setToBalance(accountTo.getBalance());
           notificationService.notifyAboutTransfer(accountFrom,"Transfer of " + transferMoneyDetails.getAmount() + " to account " + accountTo.getAccountId());
           notificationService.notifyAboutTransfer(accountTo, "Transfer of " + transferMoneyDetails.getAmount() + " from account " + accountFrom.getAccountId());

        }
        return transferCompletionDetails;
    }
}
