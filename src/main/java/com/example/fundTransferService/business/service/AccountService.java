package com.example.fundTransferService.business.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.dto.requests.AccountCreationRequest;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.dto.response.FundsTransferResponse;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.model.TransactionHistory;
import com.example.fundTransferService.business.respository.AccountHolderRepository;
import com.example.fundTransferService.business.respository.AccountRepository;
import com.example.fundTransferService.business.respository.TransactionHistoryRepository;
import com.example.fundTransferService.business.strategy.exchange.ExchangeStrategy;
import com.example.fundTransferService.business.strategy.rule.FundsTransferRule;
import com.example.fundTransferService.exception.CurrencyNotSupportedException;
import com.example.fundTransferService.exception.ExchangeStrategyException;
import com.example.fundTransferService.factory.AccountFactory;
import com.example.fundTransferService.factory.FundsTransferFactory;
import com.example.fundTransferService.factory.TransactionHistoryFactory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    private final CurrencyConversionService currencyConversionService;

    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    private final AccountFactory accountFactory;
    private final FundsTransferFactory fundsTransferFactory;
    private final TransactionHistoryFactory transactionHistoryFactory;

    private final List<FundsTransferRule> fundsTransferRules;
    private final List<ExchangeStrategy> exchangeStrategies;

    public Account createNewAccount(Long accountHolderId, AccountCreationRequest accountCreationRequest) {
        AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElseThrow();
        Account account = accountFactory.from(accountCreationRequest, accountHolder);
        accountRepository.save(account);
        return account;
    }

    public synchronized FundsTransferResponse transfer(FundsTransferRequest fundTransferRequest) {
        log.info("Attempting transfer from {} to {} an amount of {}", fundTransferRequest.getAccountToDebitIban(), fundTransferRequest.getAccountToCreditIban(), fundTransferRequest.getAmount());
        FundsTransferOrder fundsTransferOrder = fundsTransferFactory.toFundsTransferOrder(fundTransferRequest);
        fundsTransferRules.forEach(fundsTransferRule -> fundsTransferRule.checkRule(fundsTransferOrder));
        setExchangeRate(fundsTransferOrder);
        ExchangeStrategy exchangeStrategy = getValidExchangeStrategy(fundsTransferOrder);
        exchangeStrategy.execute(fundsTransferOrder);
        TransactionHistory transactionHistory = transactionHistoryFactory.toTransactionHistory(fundsTransferOrder);
        transactionHistoryRepository.save(transactionHistory);
        log.info("Transfer Completed");
        return fundsTransferFactory.toFundsTransferResponse(fundsTransferOrder, transactionHistory.getTransactionId());
    }

    private ExchangeStrategy getValidExchangeStrategy(FundsTransferOrder fundsTransferOrder) {
        Set<ExchangeStrategy> exchangeStrategySet = exchangeStrategies.stream().filter(exchangeStrategy -> exchangeStrategy.isValid(fundsTransferOrder)).collect(Collectors.toSet());
        if (exchangeStrategySet.isEmpty()) {
            throw new ExchangeStrategyException("couldn't find a valid exchange strategy to execute");
        }
        if (exchangeStrategySet.size() > 1) {
            throw new ExchangeStrategyException("Found multiple valid executions");
        }
        return exchangeStrategySet.stream().findFirst().get();
    }

    private void setExchangeRate(FundsTransferOrder fundsTransferOrder) {
        String baseCurrency = fundsTransferOrder.getAccountToDebit().getCurrency();
        String targetCurrency = fundsTransferOrder.getAccountToCredit().getCurrency();
        try {
            BigDecimal exchangeRate = currencyConversionService.getCurrentExchangeRate(baseCurrency, targetCurrency);
            fundsTransferOrder.setExchangeRate(exchangeRate);
            log.info("calculated exchange rate: " + exchangeRate);
        } catch (CurrencyNotSupportedException currencyNotSupportedException) {
            String failedCurrency = currencyNotSupportedException.getMessage();
            String iban = fundsTransferOrder.getAccountToDebit().getCurrency().equals(failedCurrency) ? fundsTransferOrder.getAccountToDebit().getIban() : fundsTransferOrder.getAccountToCredit().getIban();
            throw new CurrencyNotSupportedException(iban, failedCurrency);
        }
    }


}
