package com.dws.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.model.TransferCompletionDetails;
import com.dws.challenge.model.TransferMoneyDetails;
import com.dws.challenge.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  void addAccount() {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  void addAccount_failsOnDuplicateId() {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }
  }
  @Test
  void transfer_valid_scenario(){

    Account account = new Account("A12345");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    Account account1 = new Account("B12345");
    account1.setBalance(new BigDecimal(500));
    this.accountsService.createAccount(account1);

    TransferMoneyDetails transferMoneyDetails = TransferMoneyDetails.builder()
            .accountToId("A12345").accountFromId("B12345").amount(BigDecimal.valueOf(500)).build();

    TransferCompletionDetails transferCompletionDetails = this.accountsService.transfer(transferMoneyDetails);

    assertEquals(BigDecimal.valueOf(0),transferCompletionDetails.getFromBalance());
    assertEquals(BigDecimal.valueOf(1500),transferCompletionDetails.getToBalance());

    /*actual value in memory*/

    assertEquals(BigDecimal.valueOf(0),this.accountsService.getAccount("B12345").getBalance());
    assertEquals(BigDecimal.valueOf(1500),this.accountsService.getAccount("A12345").getBalance());

  }

  @Test
  void transfer_valid_deposit_Amount(){

    Account account = new Account("A123456");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    Account account1 = new Account("B123456");
    account1.setBalance(new BigDecimal(500));
    this.accountsService.createAccount(account1);

    TransferMoneyDetails transferMoneyDetails = TransferMoneyDetails.builder()
            .accountToId("A123456").accountFromId("B123456").amount(BigDecimal.valueOf(0)).build();

    try {
      TransferCompletionDetails transferCompletionDetails = this.accountsService.transfer(transferMoneyDetails);
    }catch (Exception e){
      assertEquals("Invalid deposit amount. Amount should be a positive number.",e.getMessage());
    }
  }

  @Test
  void transfer_insufficient_balance_Amount(){

    Account account = new Account("A1234");
    account.setBalance(new BigDecimal(500));
    this.accountsService.createAccount(account);

    Account account1 = new Account("B1234");
    account1.setBalance(new BigDecimal(100));
    this.accountsService.createAccount(account1);

    TransferMoneyDetails transferMoneyDetails = TransferMoneyDetails.builder()
            .accountToId("A1234").accountFromId("B1234").amount(BigDecimal.valueOf(500)).build();

    try {
      TransferCompletionDetails transferCompletionDetails = this.accountsService.transfer(transferMoneyDetails);
    }catch (Exception e){
      assertEquals("Account id B1234 has InSufficient Balance ",e.getMessage());
    }
  }
}
