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
        transactionHistory.setExchangeFee(fundsTransferOrder.getExchangeFee());
        transactionHistory.setTransactionId(generateTransactionId(fundsTransferOrder));
        transactionHistory.setAmount(fundsTransferOrder.getFundsTransferRequest().getAmount());
        return transactionHistory;
    }

    private String generateTransactionId(FundsTransferOrder fundsTransferOrder) {
        return fundsTransferOrder.getAccountToDebit().getId() + "_" + fundsTransferOrder.getAccountToCredit().getId() + "_" + System.currentTimeMillis() / 1000L;
    }

}
