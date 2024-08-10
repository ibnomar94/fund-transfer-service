package com.example.fundTransferService.business.rule.implementation;

import org.springframework.stereotype.Component;

import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.rule.FundsTransferRule;
import com.example.fundTransferService.exception.SameAccountException;

@Component
public class DifferentAccountsMustBeProvidedRule implements FundsTransferRule {

    public void checkRule(FundsTransferOrder fundsTransfer) {
        if (fundsTransfer.getAccountToCredit().getId().equals(fundsTransfer.getAccountToDebit().getId())) {
            throw new SameAccountException();
        }
    }
}
