package com.example.fundTransferService.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static com.example.fundTransferService.TestDataUtils.generateFundsTransferRequest;
import static com.example.fundTransferService.TestDataUtils.getFundsTransferOrder;
import static com.example.fundTransferService.exception.ExchangeStrategyException.MULTIPLE_VALID_STRATEGY;
import static com.example.fundTransferService.exception.ExchangeStrategyException.NO_VALID_STRATEGY;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fundTransferService.business.domain.Currency;
import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.respository.AccountHolderRepository;
import com.example.fundTransferService.business.respository.AccountRepository;
import com.example.fundTransferService.business.respository.TransactionHistoryRepository;
import com.example.fundTransferService.business.strategy.exchange.implementation.DifferentCurrencyStrategy;
import com.example.fundTransferService.business.strategy.exchange.implementation.SameCurrencyStrategy;
import com.example.fundTransferService.business.strategy.rule.FundsTransferRule;
import com.example.fundTransferService.exception.CurrencyNotSupportedException;
import com.example.fundTransferService.exception.ExchangeStrategyException;
import com.example.fundTransferService.factory.AccountFactory;
import com.example.fundTransferService.factory.FundsTransferFactory;
import com.example.fundTransferService.factory.TransactionHistoryFactory;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountHolderRepository accountHolderRepository;
    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private AccountFactory accountFactory;
    @Mock
    private FundsTransferFactory fundsTransferFactory;
    @Mock
    private TransactionHistoryFactory transactionHistoryFactory;

    @Mock
    private FundsTransferRule fundsTransferRules;

    @Mock
    private SameCurrencyStrategy sameCurrencyStrategy;

    @Mock
    private DifferentCurrencyStrategy differentCurrencyStrategy;

    private AccountService accountService;

    @BeforeEach
    void init() {
        accountService = new AccountService(currencyConversionService, accountRepository,
                accountHolderRepository, transactionHistoryRepository,
                accountFactory, fundsTransferFactory, transactionHistoryFactory, List.of(fundsTransferRules), List.of(sameCurrencyStrategy, differentCurrencyStrategy));
    }

    @Test
    void transfer_noValidExecutionStrategy_exceptionIsThrown() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest("LU1", "LU2", BigDecimal.TEN);
        FundsTransferOrder fundsTransferOrder = getFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.ZERO, BigDecimal.TEN);

        when(fundsTransferFactory.toFundsTransferOrder(fundsTransferRequest)).thenReturn(fundsTransferOrder);
        when(currencyConversionService.getCurrentExchangeRate(any(), any())).thenReturn(BigDecimal.ONE);

        Exception exception = assertThrows(ExchangeStrategyException.class, () -> {
            accountService.transfer(fundsTransferRequest);
        });
        assertEquals(exception.getMessage(), NO_VALID_STRATEGY);
    }

    @Test
    void transfer_multipleValidExecutionStrategy_exceptionIsThrown() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest("LU1", "LU2", BigDecimal.TEN);
        FundsTransferOrder fundsTransferOrder = getFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.ZERO, BigDecimal.TEN);

        when(fundsTransferFactory.toFundsTransferOrder(fundsTransferRequest)).thenReturn(fundsTransferOrder);
        when(currencyConversionService.getCurrentExchangeRate(any(), any())).thenReturn(BigDecimal.ONE);
        when(sameCurrencyStrategy.isValid(any())).thenReturn(true);
        when(differentCurrencyStrategy.isValid(any())).thenReturn(true);

        Exception exception = assertThrows(ExchangeStrategyException.class, () -> {
            accountService.transfer(fundsTransferRequest);
        });
        assertEquals(exception.getMessage(), MULTIPLE_VALID_STRATEGY);
    }

    @Test
    void transfer_currencyConversionServiceException_exceptionIsReformatted() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest("LU1", "LU2", BigDecimal.TEN);
        FundsTransferOrder fundsTransferOrder = getFundsTransferOrder(fundsTransferRequest, Currency.USD, Currency.EUR, BigDecimal.ZERO, BigDecimal.TEN);

        when(fundsTransferFactory.toFundsTransferOrder(fundsTransferRequest)).thenReturn(fundsTransferOrder);
        when(currencyConversionService.getCurrentExchangeRate(any(), any())).thenThrow(new CurrencyNotSupportedException(Currency.USD));

        Exception exception = assertThrows(CurrencyNotSupportedException.class, () -> {
            accountService.transfer(fundsTransferRequest);
        });
        assertEquals(exception.getMessage(), "Account with IBAN: LU1 has a currency (USD) that is not currently supported by our system");
    }

}
