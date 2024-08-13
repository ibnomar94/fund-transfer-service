package com.example.fundTransferService.unit.business.factory;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static com.example.fundTransferService.TestDataUtils.generateAccountCreationRequest;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fundTransferService.business.domain.enums.Currency;
import com.example.fundTransferService.business.dto.requests.AccountCreationRequest;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.factory.AccountFactory;

@ExtendWith(MockitoExtension.class)
public class AccountFactoryTest {

    @InjectMocks
    AccountFactory accountFactory;

    @Test
    void creatingAccount_validDataRequest_accountCreated() {
        AccountCreationRequest accountCreationRequest = generateAccountCreationRequest(Currency.EUR, BigDecimal.TEN);
        AccountHolder accountHolder = new AccountHolder();
        accountHolder.setId(23L);
        Account account = accountFactory.from(accountCreationRequest, accountHolder);

        assertEquals(account.getAccountHolder(), accountHolder);
        assertEquals(account.getCurrency(), Currency.EUR);
        assertEquals(account.getBalance(), BigDecimal.TEN);
        assertTrue(account.getIban().startsWith("LU23"));
    }

}
