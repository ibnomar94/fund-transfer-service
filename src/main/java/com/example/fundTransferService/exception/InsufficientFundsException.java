package com.example.fundTransferService.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {}

    public InsufficientFundsException(String iban) {
        super(String.format("Account with IBAN: %s does not have sufficient balance to execute this transaction", iban));
    }
}
