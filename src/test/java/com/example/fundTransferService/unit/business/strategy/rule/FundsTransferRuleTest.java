package com.example.fundTransferService.unit.business.strategy.rule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import static com.example.fundTransferService.TestDataUtils.generateFundsTransferOrder;
import static com.example.fundTransferService.TestDataUtils.generateFundsTransferRequest;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fundTransferService.business.domain.enums.Currency;
import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.strategy.rule.implementation.AccountMustExistRule;
import com.example.fundTransferService.business.strategy.rule.implementation.DifferentAccountsMustBeProvidedRule;
import com.example.fundTransferService.business.strategy.rule.implementation.SufficientFundsMustBePresentRule;
import com.example.fundTransferService.exception.InsufficientFundsException;
import com.example.fundTransferService.exception.InvalidAccountException;
import com.example.fundTransferService.exception.SameAccountException;


@ExtendWith(MockitoExtension.class)
public class FundsTransferRuleTest {

    @InjectMocks
    AccountMustExistRule accountMustExistRule;

    @InjectMocks
    DifferentAccountsMustBeProvidedRule differentAccountsMustBeProvidedRule;

    @InjectMocks
    SufficientFundsMustBePresentRule sufficientFundsMustBePresentRule;


    @Test
    void multipleRules_invalidValues_exceptionIsThrown() {

        // SufficientFundsMustBePresentRule
        BigDecimal amountToTransfer = BigDecimal.valueOf(5);
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest("LU1", "LU2", amountToTransfer);
        FundsTransferOrder insufficientTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.ONE, BigDecimal.ZERO);
        FundsTransferOrder sufficientTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.TEN, BigDecimal.ZERO);

        assertThrows(InsufficientFundsException.class, () -> {
            sufficientFundsMustBePresentRule.checkRule(insufficientTransferOrder);
        });

        try {
            sufficientFundsMustBePresentRule.checkRule(sufficientTransferOrder);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }

        // DifferentAccountsMustBeProvidedRule
        FundsTransferOrder sameAccountTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.ONE, BigDecimal.ZERO);
        sameAccountTransferOrder.getAccountToDebit().setId(1L);
        sameAccountTransferOrder.getAccountToCredit().setId(1L);
        FundsTransferOrder differentAccountTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.TEN, BigDecimal.ZERO);
        differentAccountTransferOrder.getAccountToDebit().setId(1L);
        differentAccountTransferOrder.getAccountToCredit().setId(2L);

        assertThrows(SameAccountException.class, () -> {
            differentAccountsMustBeProvidedRule.checkRule(sameAccountTransferOrder);
        });

        try {
            differentAccountsMustBeProvidedRule.checkRule(differentAccountTransferOrder);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }

        // AccountMustExistRule
        FundsTransferOrder noValidCreditAccountTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.ONE, BigDecimal.ZERO);
        FundsTransferOrder validAccountsTransferOrder = generateFundsTransferOrder(fundsTransferRequest, Currency.EUR, Currency.EUR, BigDecimal.ONE, BigDecimal.ZERO);

        noValidCreditAccountTransferOrder.setAccountToCredit(null);

        InvalidAccountException invalidAccountException = assertThrows(InvalidAccountException.class, () -> {
            accountMustExistRule.checkRule(noValidCreditAccountTransferOrder);
        });
        assertEquals(invalidAccountException.getMessage(), "IBAN: LU2 does not correspond to a valid account");

        try {
            accountMustExistRule.checkRule(validAccountsTransferOrder);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }

    }

}
