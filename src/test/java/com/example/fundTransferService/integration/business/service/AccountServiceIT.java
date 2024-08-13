package com.example.fundTransferService.integration.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.example.fundTransferService.TestDataUtils.generateFundsTransferRequest;
import static com.example.fundTransferService.TestDataUtils.generateTestAccount;
import static com.example.fundTransferService.TestDataUtils.generateTestAccountHolder;
import static com.example.fundTransferService.external.client.FreeCurrencyApiClient.API_NUMBER_OF_DECIMAL_PLACES;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.example.fundTransferService.FundTransferServiceApplication;
import com.example.fundTransferService.business.domain.enums.Currency;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.model.TransactionHistory;
import com.example.fundTransferService.business.respository.AccountHolderRepository;
import com.example.fundTransferService.business.respository.AccountRepository;
import com.example.fundTransferService.business.respository.TransactionHistoryRepository;
import com.example.fundTransferService.business.service.AccountService;
import com.example.fundTransferService.exception.InsufficientFundsException;
import com.example.fundTransferService.external.client.FreeCurrencyApiClient;

@SpringJUnitConfig
@ContextConfiguration(classes = {FundTransferServiceApplication.class})
@TestPropertySource(locations = "classpath:/application-test.properties")
@ExtendWith(MockitoExtension.class)
public class AccountServiceIT {

    public static final String DEBIT_ACCOUNT_IBAN = "LU1";
    public static final String CREDIT_ACCOUNT_IBAN = "LU2";
    public static final String CREDIT_ACCOUNT_USD_IBAN = "LU3";

    @Value("${exchange_fees}")
    private BigDecimal EXCHANGE_FEES;

    @Autowired
    private AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    FreeCurrencyApiClient freeCurrencyApiClient;

    @BeforeEach
    void init() {
        AccountHolder debitAccountHolder = generateTestAccountHolder("john", "doe", "2702", "homeTown");
        AccountHolder creditAccountHolder = generateTestAccountHolder("jane", "doe", "2912", "homeTown");
        accountHolderRepository.saveAll(List.of(debitAccountHolder, creditAccountHolder));

        Account accountToDebit = generateTestAccount(DEBIT_ACCOUNT_IBAN, Currency.EUR, BigDecimal.valueOf(200));
        accountToDebit.setAccountHolder(debitAccountHolder);
        Account accountToCredit = generateTestAccount(CREDIT_ACCOUNT_IBAN, Currency.EUR, BigDecimal.ONE);
        Account accountToUSD = generateTestAccount(CREDIT_ACCOUNT_USD_IBAN, Currency.USD, BigDecimal.ZERO);
        accountToCredit.setAccountHolder(creditAccountHolder);
        accountToUSD.setAccountHolder(creditAccountHolder);
        accountRepository.saveAll(List.of(accountToDebit, accountToCredit, accountToUSD));


    }

    @Test
    @Transactional
    void transfer_sameCurrency_correctData() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest(DEBIT_ACCOUNT_IBAN, CREDIT_ACCOUNT_IBAN, BigDecimal.TEN);
        accountService.transfer(fundsTransferRequest);
        assertEquals(accountRepository.findByIban(DEBIT_ACCOUNT_IBAN).get().getBalance(), BigDecimal.valueOf(190));
        assertEquals(accountRepository.findByIban(CREDIT_ACCOUNT_IBAN).get().getBalance(), BigDecimal.valueOf(11));
        TransactionHistory transactionHistory = transactionHistoryRepository.findAll().get(0);
        assertNotNull(transactionHistory);
    }

    @Test
    @Transactional
    void transfer_differentCurrency_correctData() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest(DEBIT_ACCOUNT_IBAN, CREDIT_ACCOUNT_USD_IBAN, BigDecimal.TEN);
        BigDecimal exchangeRate = BigDecimal.ONE.divide(freeCurrencyApiClient.getLatestRates().getCurrencyRateMap().get(Currency.EUR), API_NUMBER_OF_DECIMAL_PLACES, RoundingMode.CEILING);
        accountService.transfer(fundsTransferRequest);
        assertEquals(accountRepository.findByIban(DEBIT_ACCOUNT_IBAN).get().getBalance(), BigDecimal.valueOf(180));
        assertEquals(accountRepository.findByIban(CREDIT_ACCOUNT_USD_IBAN).get().getBalance(), BigDecimal.TEN.multiply(exchangeRate));
        TransactionHistory transactionHistory = transactionHistoryRepository.findAll().get(0);
        assertNotNull(transactionHistory);
        assertEquals(transactionHistory.getExchangeFee(), EXCHANGE_FEES);
        assertEquals(transactionHistory.getExchangeRate(), exchangeRate);
    }

    @Test
    @Transactional
    void transfer_differentCurrencyNoBalance_exception() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest(DEBIT_ACCOUNT_IBAN, CREDIT_ACCOUNT_USD_IBAN, BigDecimal.valueOf(500));
        Exception exception = assertThrows(InsufficientFundsException.class, () -> {
            accountService.transfer(fundsTransferRequest);
        });
        assertEquals(accountRepository.findByIban(DEBIT_ACCOUNT_IBAN).get().getBalance(), BigDecimal.valueOf(200));
        assertEquals(accountRepository.findByIban(CREDIT_ACCOUNT_USD_IBAN).get().getBalance(), BigDecimal.ZERO);
        assertEquals(transactionHistoryRepository.findAll().size(), 0);
        assertEquals(exception.getMessage(),"Account with IBAN: LU1 does not have sufficient balance to execute this transaction");
    }


}
