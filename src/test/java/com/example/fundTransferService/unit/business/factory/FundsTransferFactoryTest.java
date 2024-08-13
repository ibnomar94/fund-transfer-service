package com.example.fundTransferService.unit.business.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import static com.example.fundTransferService.TestDataUtils.generateFundsTransferOrder;
import static com.example.fundTransferService.TestDataUtils.generateFundsTransferRequest;
import static com.example.fundTransferService.TestDataUtils.generateTestAccount;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fundTransferService.business.domain.enums.Currency;
import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.dto.response.FundsTransferResponse;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.respository.AccountRepository;
import com.example.fundTransferService.factory.FundsTransferFactory;

@ExtendWith(MockitoExtension.class)
public class FundsTransferFactoryTest {

    @InjectMocks
    private FundsTransferFactory fundsTransferFactory;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void toFundsTransferOrder_validData_correctMapping() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest("LU1", "LU2", BigDecimal.TEN);
        Account accountToDebit = generateTestAccount("LU1", Currency.EUR, BigDecimal.ONE);
        Account accountToCredit = generateTestAccount("LU2", Currency.EUR, BigDecimal.ONE);
        when(accountRepository.findByIban("LU1")).thenReturn(Optional.of(accountToDebit));
        when(accountRepository.findByIban("LU2")).thenReturn(Optional.of(accountToCredit));
        FundsTransferOrder fundsTransferOrder = fundsTransferFactory.toFundsTransferOrder(fundsTransferRequest);

        assertEquals(fundsTransferOrder.getFundsTransferRequest(), fundsTransferRequest);
        assertEquals(fundsTransferOrder.getAccountToDebit(), accountToDebit);
        assertEquals(fundsTransferOrder.getAccountToCredit(), accountToCredit);
    }

    @Test
    void toFundsTransferResponse_validData_correctMapping() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest("LU1", "LU2", BigDecimal.TEN);
        FundsTransferOrder fundsTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.ZERO, BigDecimal.TEN);
        fundsTransferOrder.setExchangeFee(BigDecimal.valueOf(2L));
        fundsTransferOrder.setExchangeRate(BigDecimal.valueOf(3L));
        FundsTransferResponse fundsTransferResponse = fundsTransferFactory.toFundsTransferResponse(fundsTransferOrder, "TX_1");

        assertEquals(fundsTransferResponse.getExchangeFee(), BigDecimal.valueOf(2L));
        assertEquals(fundsTransferResponse.getExchangeRate(), BigDecimal.valueOf(3L));
        assertEquals(fundsTransferResponse.getTransactionId(), "TX_1");

    }
}
