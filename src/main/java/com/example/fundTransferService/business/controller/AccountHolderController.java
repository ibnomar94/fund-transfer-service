package com.example.fundTransferService.business.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.fundTransferService.business.dto.requests.AccountHolderCreationRequest;
import com.example.fundTransferService.business.dto.response.AccountHolderCreationResponse;
import com.example.fundTransferService.business.dto.response.AccountHolderDetailsResponse;
import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.service.AccountHolderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("account-holder")
@Tag(name = "Accounts Holder Resource")
public class AccountHolderController {

    private final AccountHolderService accountHolderService;

    @Operation(summary = "Lists All accounts in the system")
    @GetMapping(path = "/list")
    public List<AccountHolderDetailsResponse> listAllAccountHolders() {
        return accountHolderService.getAllAccountHolders();
    }

    @Operation(summary = "Create New Account Holder")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountHolderCreationResponse> createAccountHolder(@RequestBody @Valid AccountHolderCreationRequest accountHolderCreationRequest) {
        return new ResponseEntity<>(accountHolderService.createAccountHolder(accountHolderCreationRequest), HttpStatus.CREATED);
    }

}
