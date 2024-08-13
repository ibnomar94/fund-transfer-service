package com.example.fundTransferService.unit.business.strategy.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static com.example.fundTransferService.TestDataUtils.generateFundsTransferOrder;
import static com.example.fundTransferService.TestDataUtils.generateFundsTransferRequest;

import java.math.BigDecimal;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fundTransferService.business.domain.enums.Currency;
import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.strategy.exchange.implementation.DifferentCurrencyStrategy;
import com.example.fundTransferService.business.strategy.exchange.implementation.SameCurrencyStrategy;
import com.example.fundTransferService.exception.InsufficientFundsException;


@ExtendWith(MockitoExtension.class)
public class ExchangeStrategyTest {

    @InjectMocks
    SameCurrencyStrategy sameCurrencyStrategy;

    @InjectMocks
    DifferentCurrencyStrategy differentCurrencyStrategy;

    private final BigDecimal exchangeFees = BigDecimal.valueOf(0.25);

    @BeforeEach
    public void before() throws Exception {
        FieldUtils.writeField(differentCurrencyStrategy, "EXCHANGE_FEES", exchangeFees, true);
    }

    @Test
    void multipleExchangeStrategies_test() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest("LU1", "LU2", BigDecimal.valueOf(5));
        FundsTransferOrder sameCurrencyTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.TEN, BigDecimal.ONE);
        FundsTransferOrder differentCurrencyTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.USD, Currency.EUR, BigDecimal.TEN, BigDecimal.ONE);

        assertTrue(sameCurrencyStrategy.isValid(sameCurrencyTransferOrder));
        assertFalse(differentCurrencyStrategy.isValid(sameCurrencyTransferOrder));

        assertTrue(differentCurrencyStrategy.isValid(differentCurrencyTransferOrder));
        assertFalse(sameCurrencyStrategy.isValid(differentCurrencyTransferOrder));
    }

    @Test
    void multipleExchangeStrategies_insufficientFund_exceptionIsThrown() {
        BigDecimal amountToTransfer = BigDecimal.valueOf(5);
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest("LU1", "LU2", amountToTransfer);
        FundsTransferOrder sameCurrencyTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.ONE, BigDecimal.ZERO);
        FundsTransferOrder differentCurrencyTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.USD, Currency.EUR, BigDecimal.ONE, BigDecimal.ZERO);
        FundsTransferOrder differentCurrencyTransferOrderWithInitialSufficientBalance = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.USD, amountToTransfer, BigDecimal.ZERO);

        assertThrows(InsufficientFundsException.class, () -> {
            sameCurrencyStrategy.execute(sameCurrencyTransferOrder);
        });

        assertThrows(InsufficientFundsException.class, () -> {
            differentCurrencyStrategy.execute(differentCurrencyTransferOrder);
        });

        // should fail because debited account should have sufficient amount that covers 'transaction amount' + 'exchange fees'
        assertThrows(InsufficientFundsException.class, () -> {
            differentCurrencyStrategy.execute(differentCurrencyTransferOrderWithInitialSufficientBalance);
        });

    }

    @Test
    void multipleExchangeStrategies_sufficientFund_correctBehaviour() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest("LU1", "LU2", BigDecimal.valueOf(4));
        FundsTransferOrder sameCurrencyTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.TEN, BigDecimal.ONE);
        FundsTransferOrder differentCurrencyTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.USD, BigDecimal.TEN, BigDecimal.ZERO);
        differentCurrencyTransferOrder.setExchangeRate(BigDecimal.valueOf(1.1));

        sameCurrencyStrategy.execute(sameCurrencyTransferOrder);
        assertEquals(sameCurrencyTransferOrder.getAccountToDebit().getBalance(), BigDecimal.valueOf(6));
        assertEquals(sameCurrencyTransferOrder.getAccountToCredit().getBalance(), BigDecimal.valueOf(5)); // initial 1 euro + transferred 4 euros

        differentCurrencyStrategy.execute(differentCurrencyTransferOrder);
        assertEquals(differentCurrencyTransferOrder.getAccountToDebit().getBalance(), BigDecimal.valueOf(5.75));
        assertEquals(differentCurrencyTransferOrder.getAccountToCredit().getBalance(), BigDecimal.valueOf(4.4));
        assertEquals(differentCurrencyTransferOrder.getExchangeFee(), exchangeFees);
    }

}
