package com.example.fundTransferService.exception;

public class UnableToRetrieveExchangeRateException extends RuntimeException {

    public UnableToRetrieveExchangeRateException() {
    }

    public UnableToRetrieveExchangeRateException(String message) {
        super(message);
    }
}
