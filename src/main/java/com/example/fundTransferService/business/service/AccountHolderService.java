package com.example.fundTransferService.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.respository.AccountHolderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountHolderService {

    private final AccountHolderRepository accountHolderRepository;

    public List<AccountHolder> getAllAccountHolders() {
        return accountHolderRepository.findAll();
    }

}
