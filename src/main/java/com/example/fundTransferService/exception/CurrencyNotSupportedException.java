package com.example.fundTransferService.exception;

import com.example.fundTransferService.business.domain.Currency;

public class CurrencyNotSupportedException extends RuntimeException {

    public CurrencyNotSupportedException(String currency) {
        super(String.format("(%1$s) that is not currently supported by our system", currency));
    }

    public CurrencyNotSupportedException(Currency currency) {
        super(currency.toString());
    }

    public CurrencyNotSupportedException(String iban, Currency currency) {
        super(String.format("Account with IBAN: %1$s has a currency (%2$s) that is not currently supported by our system", iban, currency.toString()));
    }

}
