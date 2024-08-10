package com.example.fundTransferService.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.example.fundTransferService.business.rule.FundsTransferRule;
import com.example.fundTransferService.exception.CurrencyNotSupportedException;
import com.example.fundTransferService.factory.AccountFactory;
import com.example.fundTransferService.factory.FundsTransferFactory;
import com.example.fundTransferService.factory.TransactionHistoryFactory;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AccountService {

    private final CurrencyConversionService currencyConversionService;

    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    private final AccountFactory accountFactory;
    private final FundsTransferFactory fundsTransferFactory;
    private final TransactionHistoryFactory transactionHistoryFactory;

    private final List<FundsTransferRule> fundsTransferRules;


    public Account createNewAccount(Long accountHolderId, AccountCreationRequest accountCreationRequest) {
        AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElseThrow();
        Account account = accountFactory.from(accountCreationRequest, accountHolder);
        accountRepository.save(account);
        return account;
    }

    public FundsTransferResponse transfer(FundsTransferRequest fundTransferRequest) {
        FundsTransferOrder fundsTransferOrder = fundsTransferFactory.toFundsTransferOrder(fundTransferRequest);
        fundsTransferRules.forEach(fundsTransferRule -> fundsTransferRule.checkRule(fundsTransferOrder));
        setExchangeRate(fundsTransferOrder);
        executeTransfer(fundsTransferOrder);
        TransactionHistory transactionHistory = transactionHistoryFactory.toTransactionHistory(fundsTransferOrder);
        transactionHistoryRepository.save(transactionHistory);
        return new FundsTransferResponse(transactionHistory.getTransactionId(),transactionHistory.getExchangeRate());
    }

    private void executeTransfer(FundsTransferOrder fundsTransferOrder) {
        fundsTransferOrder.getAccountToDebit().debit(fundsTransferOrder.getFundsTransferRequest().getAmount());
        fundsTransferOrder.getAccountToCredit().credit(fundsTransferOrder.getTrueValueOfTransfer());
    }

    private void setExchangeRate(FundsTransferOrder fundsTransferOrder) {
        String baseCurrency = fundsTransferOrder.getAccountToDebit().getCurrency();
        String targetCurrency = fundsTransferOrder.getAccountToCredit().getCurrency();
        try {
            fundsTransferOrder.setExchangeRate(currencyConversionService.getCurrentExchangeRate(baseCurrency, targetCurrency));
        } catch (CurrencyNotSupportedException currencyNotSupportedException) {
            String failedCurrency = currencyNotSupportedException.getMessage();
            String iban = fundsTransferOrder.getAccountToDebit().getCurrency().equals(failedCurrency) ? fundsTransferOrder.getAccountToDebit().getIban() : fundsTransferOrder.getAccountToCredit().getIban();
            throw new CurrencyNotSupportedException(iban, failedCurrency);
        }
    }


}
