package com.dws.challenge.validate;

import com.dws.challenge.domain.Account;
import com.dws.challenge.model.TransferMoneyDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountValidatorTest {

    @Autowired
    private AccountValidator accountValidator;

    @Test
    void validate_from_acc_test() {
        TransferMoneyDetails transferMoneyDetail = TransferMoneyDetails.builder()
                .build();
        try{
            accountValidator.validate(transferMoneyDetail);
        }catch (Exception e){
            assertEquals("From account id required",e.getMessage());
        }


    }

    @Test
    void validate_to_acc_test() {
        TransferMoneyDetails transferMoneyDetail = TransferMoneyDetails.builder()
                .accountFromId("A1234")
                .build();
        try{
            accountValidator.validate(transferMoneyDetail);
        }catch (Exception e){
            assertEquals("To account id required",e.getMessage());
        }


    }

    @Test
    void validate_amount_test() {
        TransferMoneyDetails transferMoneyDetail = TransferMoneyDetails.builder()
                .accountFromId("A1234")
                .accountToId("B1234")
                .build();
        try{
            accountValidator.validate(transferMoneyDetail);
        }catch (Exception e){
            assertEquals("Amount id required",e.getMessage());
        }


    }

    @Test
    void validate_acc_notsame_test() {
        TransferMoneyDetails transferMoneyDetail = TransferMoneyDetails.builder()
                .accountFromId("A1234")
                .accountToId("A1234")
                .amount(BigDecimal.valueOf(500))
                .build();
        try{
            accountValidator.validate(transferMoneyDetail);
        }catch (Exception e){
            assertEquals("From accountId and To accountId can't be same",e.getMessage());
        }


    }
}
