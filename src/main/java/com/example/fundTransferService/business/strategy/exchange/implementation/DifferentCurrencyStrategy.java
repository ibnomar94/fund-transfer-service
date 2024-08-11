package com.example.fundTransferService.business.strategy.exchange.implementation;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.strategy.exchange.ExchangeStrategy;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DifferentCurrencyStrategy implements ExchangeStrategy {

    @Value("${exchange_fees}")
    private BigDecimal EXCHANGE_FEES;

    @Override
    public boolean isValid(FundsTransferOrder fundsTransfer) {
        return !fundsTransfer.getAccountToDebit().getCurrency().equals(fundsTransfer.getAccountToCredit().getCurrency());
    }

    @Override
    public synchronized  void execute(FundsTransferOrder fundsTransferOrder) {
        log.info("DifferentCurrencyStrategy was selected");
        BigDecimal amountToDebit = fundsTransferOrder.getFundsTransferRequest().getAmount().add(EXCHANGE_FEES);
        fundsTransferOrder.getAccountToDebit().debit(amountToDebit);
        fundsTransferOrder.getAccountToCredit().credit(fundsTransferOrder.getTrueValueOfTransfer());
        fundsTransferOrder.setExchangeFee(EXCHANGE_FEES);
    }
}
