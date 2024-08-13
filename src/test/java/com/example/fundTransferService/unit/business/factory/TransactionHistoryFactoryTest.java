package com.example.fundTransferService.unit.business.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static com.example.fundTransferService.TestDataUtils.generateFundsTransferOrder;
import static com.example.fundTransferService.TestDataUtils.generateFundsTransferRequest;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fundTransferService.business.domain.Currency;
import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.model.TransactionHistory;
import com.example.fundTransferService.factory.TransactionHistoryFactory;

@ExtendWith(MockitoExtension.class)
public class TransactionHistoryFactoryTest {

    @InjectMocks
    TransactionHistoryFactory transactionHistoryFactory;

    @Test
    void creatingTransactionHistory_validData_historyRecordCreated() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest("LU1", "LU2", BigDecimal.TEN);
        FundsTransferOrder fundsTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.ZERO, BigDecimal.TEN);
        fundsTransferOrder.setExchangeFee(BigDecimal.valueOf(2L));
        fundsTransferOrder.setExchangeRate(BigDecimal.valueOf(3L));
        fundsTransferOrder.getAccountToDebit().setId(1L);
        fundsTransferOrder.getAccountToCredit().setId(2L);

        TransactionHistory transactionHistory = transactionHistoryFactory.toTransactionHistory(fundsTransferOrder);

        assertEquals(transactionHistory.getAmount(), fundsTransferRequest.getAmount());
        assertEquals(transactionHistory.getExchangeRate(), BigDecimal.valueOf(3L));
        assertEquals(transactionHistory.getExchangeFee(), BigDecimal.valueOf(2L));
        assertEquals(transactionHistory.getDebitedAccount().getIban(), "LU1");
        assertEquals(transactionHistory.getCreditedAccount().getIban(), "LU2");
        assertTrue(transactionHistory.getTransactionId().startsWith("1_2"));

    }

}
