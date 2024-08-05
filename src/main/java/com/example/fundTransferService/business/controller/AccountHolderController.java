package com.example.fundTransferService.business.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.service.AccountHolderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("account-holder")
public class AccountHolderController {

    private final AccountHolderService accountHolderService;

    @GetMapping(path = "/list")
    public List<AccountHolder> listAllAccountHolders() {
        return accountHolderService.getAllAccountHolders();
    }

}
