package com.example.fundTransferService.factory;

import org.springframework.stereotype.Service;

import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.model.TransactionHistory;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class TransactionHistoryFactory {

    public TransactionHistory toTransactionHistory(FundsTransferOrder fundsTransferOrder) {
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setCreditedAccount(fundsTransferOrder.getAccountToCredit());
        transactionHistory.setDebitedAccount(fundsTransferOrder.getAccountToDebit());
        transactionHistory.setExchangeRate(fundsTransferOrder.getExchangeRate());
        transactionHistory.setTransactionId(generateTransactionId(fundsTransferOrder));
        return transactionHistory;
    }

    private String generateTransactionId(FundsTransferOrder fundsTransferOrder) {
        return fundsTransferOrder.getAccountToCredit().getId() + "_" + fundsTransferOrder.getAccountToDebit().getId() + "_" + System.currentTimeMillis() / 1000L;
    }

}
