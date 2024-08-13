package com.example.fundTransferService.business.dto.requests;


import java.math.BigDecimal;

import com.example.fundTransferService.business.domain.enums.Currency;
import com.example.fundTransferService.exception.CurrencyNotSupportedException;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreationRequest {

    private Currency accountCurrency;

    @NotNull
    private String currency;

    @NotNull
    @PositiveOrZero
    private BigDecimal initialBalance;


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
