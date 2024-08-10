package com.example.fundTransferService.business.domain;

import java.math.BigDecimal;

import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.model.Account;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FundsTransferOrder {
    private FundsTransferRequest fundsTransferRequest;
    private Account accountToDebit;
    private Account accountToCredit;
    private BigDecimal exchangeRate;

    public FundsTransferOrder(FundsTransferRequest fundsTransferRequest) {
        this.fundsTransferRequest = fundsTransferRequest;
    }

    public BigDecimal getTrueValueOfTransfer() {
        return exchangeRate.multiply(fundsTransferRequest.getAmount());
    }


}
