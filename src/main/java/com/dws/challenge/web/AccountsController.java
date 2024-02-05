package com.dws.challenge.web;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.exception.InvalidAccountDetailsException;
import com.dws.challenge.exception.InvalidTransferDetailsException;
import com.dws.challenge.model.TransferCompletionDetails;
import com.dws.challenge.model.TransferMoneyDetails;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.validate.AccountValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;

  private final AccountValidator accountValidator;

  @Autowired
  public AccountsController(AccountsService accountsService, AccountValidator accountValidator) {
    this.accountsService = accountsService;
    this.accountValidator = accountValidator;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
      accountValidator.validateAccount(account);
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException | InvalidAccountDetailsException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }

  @RequestMapping (value = "/transfer",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TransferCompletionDetails> transferMoney(@RequestBody TransferMoneyDetails transferMoneyDetails) throws InvalidTransferDetailsException {

        accountValidator.validate(transferMoneyDetails);
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountsService.transfer(transferMoneyDetails));

  }
}
