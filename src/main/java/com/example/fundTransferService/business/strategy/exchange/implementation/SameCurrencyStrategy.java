package com.example.fundTransferService.business.strategy.exchange.implementation;

import org.springframework.stereotype.Component;

import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.strategy.exchange.ExchangeStrategy;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SameCurrencyStrategy implements ExchangeStrategy {

    @Override
    public boolean isValid(FundsTransferOrder fundsTransfer) {
        return fundsTransfer.getAccountToDebit().getCurrency().equals(fundsTransfer.getAccountToCredit().getCurrency());
    }

    @Override
    public synchronized void execute(FundsTransferOrder fundsTransferOrder) {
        log.info("SameCurrencyStrategy was selected");
        fundsTransferOrder.getAccountToDebit().debit(fundsTransferOrder.getFundsTransferRequest().getAmount());
        fundsTransferOrder.getAccountToCredit().credit(fundsTransferOrder.getFundsTransferRequest().getAmount());
    }
}
