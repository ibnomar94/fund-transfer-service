package com.example.fundTransferService.exception;

public class InvalidAccountException extends RuntimeException {

    public InvalidAccountException(String iban) {
        super(String.format("IBAN: %s does not correspond to a valid account", iban));
    }

}
