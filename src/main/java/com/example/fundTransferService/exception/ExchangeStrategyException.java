package com.example.fundTransferService.exception;

public class ExchangeStrategyException extends RuntimeException {

    public static final String NO_VALID_STRATEGY = "Couldn't find a valid exchange strategy to execute";
    public static final String MULTIPLE_VALID_STRATEGY = "Found multiple valid executions";

    public ExchangeStrategyException() {
    }

    public ExchangeStrategyException(String message) {
        super(message);
    }
}
