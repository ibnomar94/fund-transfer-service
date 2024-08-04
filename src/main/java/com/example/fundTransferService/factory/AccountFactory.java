package com.example.fundTransferService.factory;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.example.fundTransferService.business.dto.requests.AccountCreationRequest;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.model.AccountHolder;

@Service
public class AccountFactory {


    public static final String LUXEMBOURG_INITIALS = "LU";

    public Account from(AccountCreationRequest accountCreationRequest, AccountHolder accountHolder) {
        Account account = new Account();
        account.setCurrency(accountCreationRequest.getCurrency());
        account.setIban(generateValidIban(accountHolder));
        account.setAccountHolder(accountHolder);
        account.setBalance(BigDecimal.valueOf(0.0));
        return account;
    }


    private String generateValidIban(AccountHolder accountHolder) {
        return LUXEMBOURG_INITIALS + accountHolder.getId() + Instant.now().getEpochSecond();
    }
}
