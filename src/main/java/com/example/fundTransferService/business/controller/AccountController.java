package com.example.fundTransferService.business.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fundTransferService.business.dto.requests.AccountCreationRequest;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.dto.response.AccountCreationResponse;
import com.example.fundTransferService.business.dto.response.FundsTransferResponse;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/add/{accountHolderId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountCreationResponse> addNewAccount(@PathVariable Long accountHolderId, @RequestBody AccountCreationRequest accountCreationRequest) {
        Account account = accountService.createNewAccount(accountHolderId, accountCreationRequest);
        AccountCreationResponse accountCreationResponse = new AccountCreationResponse(account.getIban());
        return new ResponseEntity<>(accountCreationResponse, HttpStatus.CREATED);
    }

    @PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FundsTransferResponse> transfer(@RequestBody FundsTransferRequest fundTransferRequest) {
        FundsTransferResponse fundTransferResponse = accountService.transfer(fundTransferRequest);
        return new ResponseEntity<>(fundTransferResponse, HttpStatus.OK);
    }

}
