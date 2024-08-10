package com.example.fundTransferService.business.rule;

import com.example.fundTransferService.business.domain.FundsTransferOrder;

public interface FundsTransferRule {

    void checkRule(FundsTransferOrder fundsTransfer);
}
