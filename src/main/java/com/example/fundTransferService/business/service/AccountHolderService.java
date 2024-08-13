package com.example.fundTransferService.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fundTransferService.business.dto.requests.AccountHolderCreationRequest;
import com.example.fundTransferService.business.dto.response.AccountHolderCreationResponse;
import com.example.fundTransferService.business.dto.response.AccountHolderDetailsResponse;
import com.example.fundTransferService.business.mapper.AccountHolderMapper;
import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.respository.AccountHolderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountHolderService {

    private final AccountHolderRepository accountHolderRepository;
    private final AccountHolderMapper accountHolderMapper;

    public List<AccountHolderDetailsResponse> getAllAccountHolders() {
        return accountHolderMapper.accountHoldersToAccountHolderDetailsResponseList(accountHolderRepository.findAll());
    }

    public AccountHolderCreationResponse createAccountHolder(AccountHolderCreationRequest accountHolderCreationRequest) {
        AccountHolder accountHolder = accountHolderMapper.fromAccountHolderCreationRequest(accountHolderCreationRequest);
        accountHolderRepository.save(accountHolder);
        return accountHolderMapper.accountHolderToAccountCreationResponse(accountHolder);
    }
}
