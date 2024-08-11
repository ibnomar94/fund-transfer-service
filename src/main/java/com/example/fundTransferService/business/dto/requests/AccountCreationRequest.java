package com.example.fundTransferService.business.dto.requests;


import com.example.fundTransferService.business.domain.Currency;
import com.example.fundTransferService.exception.CurrencyNotSupportedException;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AccountCreationRequest {

    private Currency accountCurrency;

    @NotNull
    private String currency;


    /*
    * The purpose is to have more granular control over the validity of the request value and the handling of invalid values
    * */
    public void setCurrency(String currency) {
        this.currency = currency;
        try {
            this.accountCurrency = Currency.valueOf(currency);
        } catch (IllegalArgumentException ex) {
            throw new CurrencyNotSupportedException(currency);
        }
    }
}
