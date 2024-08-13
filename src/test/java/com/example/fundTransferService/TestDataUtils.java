package com.example.fundTransferService;

import java.math.BigDecimal;

import com.example.fundTransferService.business.domain.Currency;
import com.example.fundTransferService.business.domain.FundsTransferOrder;
import com.example.fundTransferService.business.dto.requests.AccountCreationRequest;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.model.TransactionHistory;

public class TestDataUtils {

    public static Account generateTestAccount(String iban, Currency currency, BigDecimal balance) {
        Account account = new Account();
        account.setIban(iban);
        account.setAccountHolder(new AccountHolder());
        account.setCurrency(currency);
        account.setBalance(balance);
        return account;
    }

    public static AccountHolder generateTestAccountHolder(String firstName, String lastName, String ssn, String mainAddress) {
        AccountHolder accountHolder = new AccountHolder();
        accountHolder.setFirstName(firstName);
        accountHolder.setLastName(lastName);
        accountHolder.setSsn(ssn);
        accountHolder.setMainAddress(mainAddress);
        return accountHolder;
    }

    public static FundsTransferRequest generateFundsTransferRequest(String accountToDebitIban, String accountToCreditIban, BigDecimal amount) {
        FundsTransferRequest fundsTransferRequest = new FundsTransferRequest();
        fundsTransferRequest.setAmount(amount);
        fundsTransferRequest.setAccountToDebitIban(accountToDebitIban);
        fundsTransferRequest.setAccountToCreditIban(accountToCreditIban);
        return fundsTransferRequest;
    }

    public static TransactionHistory generateTransactionHistory(String transactionId) {
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTransactionId(transactionId);
        return transactionHistory;
    }

    public static AccountCreationRequest generateAccountCreationRequest(Currency currency, BigDecimal initialBalance) {
        AccountCreationRequest accountCreationRequest = new AccountCreationRequest();
        accountCreationRequest.setCurrency(currency.toString());
        accountCreationRequest.setInitialBalance(initialBalance);
        return accountCreationRequest;
    }

    public static FundsTransferOrder generateFundsTransferOrder(FundsTransferRequest fundsTransferRequest, Currency debitAccountCurrency, Currency creditAccountCurrency, BigDecimal debitAccountAmount, BigDecimal creditAccountAmount) {
        FundsTransferOrder fundsTransferOrder = new FundsTransferOrder(fundsTransferRequest);
        fundsTransferOrder.setAccountToDebit(generateTestAccount(fundsTransferRequest.getAccountToDebitIban(), debitAccountCurrency, debitAccountAmount));
        fundsTransferOrder.setAccountToCredit(generateTestAccount(fundsTransferRequest.getAccountToCreditIban(), creditAccountCurrency, creditAccountAmount));
        return fundsTransferOrder;
    }
}
