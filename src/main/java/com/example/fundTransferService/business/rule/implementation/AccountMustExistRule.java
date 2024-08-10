package com.example.fundTransferService.business.rule.implementation;

import org.springframework.stereotype.Component;

import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.rule.FundsTransferRule;
import com.example.fundTransferService.exception.InvalidAccountException;

@Component
public class AccountMustExistRule implements FundsTransferRule {

    public void checkRule(FundsTransferOrder fundsTransfer) {
        if (fundsTransfer.getAccountToCredit() == null) {
            throw new InvalidAccountException(fundsTransfer.getFundsTransferRequest().getAccountToCreditIban());
        }
        if (fundsTransfer.getAccountToDebit() == null) {
            throw new InvalidAccountException(fundsTransfer.getFundsTransferRequest().getAccountToDebitIban());
        }
    }
}
