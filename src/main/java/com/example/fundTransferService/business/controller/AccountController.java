package com.example.fundTransferService.business.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.fundTransferService.business.dto.requests.AccountCreationRequest;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.dto.response.AccountCreationResponse;
import com.example.fundTransferService.business.dto.response.FundsTransferResponse;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("account")
@Tag(name="Accounts Resource")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Creates a new account for the referenced Account Holder",
            description= "Account Holder must exist", parameters = @Parameter(
            name =  "accountHolderId",
            description  = "Account owner ID",
            example = "23",
            required = true))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/add/{accountHolderId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountCreationResponse> addNewAccount(@PathVariable Long accountHolderId, @RequestBody @Valid AccountCreationRequest accountCreationRequest) {
        Account account = accountService.createNewAccount(accountHolderId, accountCreationRequest);
        AccountCreationResponse accountCreationResponse = new AccountCreationResponse(account.getIban());
        return new ResponseEntity<>(accountCreationResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Transfer Funds between two accounts",
            description= "Funds are transferred from the debited account to the credited account, if the credited account has a different currency then an exchange of currency operation is performed")
    @PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FundsTransferResponse> transfer(@RequestBody @Valid FundsTransferRequest fundTransferRequest) {
        return new ResponseEntity<>(accountService.transfer(fundTransferRequest), HttpStatus.OK);
    }

}
