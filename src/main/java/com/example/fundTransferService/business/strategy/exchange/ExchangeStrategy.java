package com.example.fundTransferService.business.strategy.exchange;

import com.example.fundTransferService.business.domain.FundsTransferOrder;

public interface ExchangeStrategy {

    boolean isValid(FundsTransferOrder fundsTransfer);

    void execute(FundsTransferOrder fundsTransferOrder);
}
