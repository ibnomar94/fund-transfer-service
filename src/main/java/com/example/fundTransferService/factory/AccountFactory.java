package com.example.fundTransferService.factory;

import java.time.Instant;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.fundTransferService.business.dto.requests.AccountCreationRequest;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.model.AccountHolder;

@Service
public class AccountFactory {

    public static final String LUXEMBOURG_INITIALS = "LU";

    public Account from(AccountCreationRequest accountCreationRequest, AccountHolder accountHolder) {
        Account account = new Account();
        account.setCurrency(accountCreationRequest.getAccountCurrency());
        account.setIban(generateValidIban(accountHolder));
        account.setAccountHolder(accountHolder);
        account.setBalance(accountCreationRequest.getInitialBalance());
        return account;
    }


    /*
    * In a real world IBAN is generated in a way that guarantees no collision of values
    * */
    private String generateValidIban(AccountHolder accountHolder) {
        Random random = new Random();
        return LUXEMBOURG_INITIALS + accountHolder.getId() + Instant.now().getEpochSecond() + (random.nextInt(900) + 100);
    }
}
