package com.example.fundTransferService.exception;

public class SameAccountException extends RuntimeException {

    public SameAccountException() {
        super("You can't use the same account for both credit and debit action");
    }

}
