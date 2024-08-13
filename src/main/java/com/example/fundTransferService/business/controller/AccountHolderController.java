package com.example.fundTransferService.business.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.service.AccountHolderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("account-holder")
@Tag(name = "Accounts Holder Resource")
public class AccountHolderController {

    private final AccountHolderService accountHolderService;

    @Operation(summary = "Lists All accounts in the system")
    @GetMapping(path = "/list")
    public List<AccountHolder> listAllAccountHolders() {
        return accountHolderService.getAllAccountHolders();
    }

}
