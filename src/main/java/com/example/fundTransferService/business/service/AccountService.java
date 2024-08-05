package com.example.fundTransferService.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fundTransferService.business.dto.requests.AccountCreationRequest;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.respository.AccountHolderRepository;
import com.example.fundTransferService.business.respository.AccountRepository;
import com.example.fundTransferService.factory.AccountFactory;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final AccountFactory accountFactory;
    private final CurrencyConversionService currencyConversionService;


    public Account createNewAccount(Long accountHolderId, AccountCreationRequest accountCreationRequest) {
        AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElseThrow();
        Account account = accountFactory.from(accountCreationRequest, accountHolder);
        accountRepository.save(account);
        return account;
    }

}
