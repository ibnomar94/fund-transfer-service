package com.example.fundTransferService.exception;

public class CurrencyNotSupportedException extends RuntimeException {

    public CurrencyNotSupportedException(String currency) {
        super(currency);
    }

    public CurrencyNotSupportedException(String iban, String currency) {
        super(String.format("Account with IBAN: %1$s has a currency (%2$s) that is not currently supported by our system", iban, currency));
    }

}
