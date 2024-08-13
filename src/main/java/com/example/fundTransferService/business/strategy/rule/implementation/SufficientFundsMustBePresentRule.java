package com.example.fundTransferService.business.strategy.rule.implementation;

import org.springframework.stereotype.Component;

import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.strategy.rule.FundsTransferRule;
import com.example.fundTransferService.exception.InsufficientFundsException;

@Component
public class SufficientFundsMustBePresentRule implements FundsTransferRule {

    public void checkRule(FundsTransferOrder fundsTransfer) {
        if (fundsTransfer.getAccountToDebit().getBalance().compareTo(fundsTransfer.getFundsTransferRequest().getAmount()) < 0) {
            throw new InsufficientFundsException(fundsTransfer.getAccountToDebit().getIban());
        }
    }
}
