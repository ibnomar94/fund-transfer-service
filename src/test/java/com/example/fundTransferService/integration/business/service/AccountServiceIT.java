package com.example.fundTransferService.integration.business.service;

import static com.example.fundTransferService.TestDataUtils.generateFundsTransferRequest;
import static com.example.fundTransferService.TestDataUtils.generateTestAccount;
import static com.example.fundTransferService.TestDataUtils.generateTestAccountHolder;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.example.fundTransferService.FundTransferServiceApplication;
import com.example.fundTransferService.business.domain.Currency;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.respository.AccountHolderRepository;
import com.example.fundTransferService.business.respository.AccountRepository;
import com.example.fundTransferService.business.service.AccountService;

@SpringJUnitConfig
@ContextConfiguration(classes = {FundTransferServiceApplication.class})
@TestPropertySource(locations = "classpath:/application-test.properties")
public class AccountServiceIT {

    public static final String DEBIT_ACCOUNT_IBAN = "LU1";
    public static final String CREDIT_ACCOUNT_IBAN = "LU2";

    @Autowired
    private AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @BeforeEach
    void init() {
        AccountHolder debitAccountHolder = generateTestAccountHolder("john", "doe", "2702", "homeTown");
        AccountHolder creditAccountHolder = generateTestAccountHolder("jane", "doe", "2912", "homeTown");
        accountHolderRepository.saveAll(List.of(debitAccountHolder, creditAccountHolder));

        Account accountToDebit = generateTestAccount(DEBIT_ACCOUNT_IBAN, Currency.EUR, BigDecimal.TEN);
        accountToDebit.setAccountHolder(debitAccountHolder);
        Account accountToCredit = generateTestAccount(CREDIT_ACCOUNT_IBAN, Currency.EUR, BigDecimal.ONE);
        accountToCredit.setAccountHolder(creditAccountHolder);
        accountRepository.saveAll(List.of(accountToDebit, accountToCredit));
    }

    @Test
    void transfer_sameCurrency_correctData() {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest(DEBIT_ACCOUNT_IBAN, CREDIT_ACCOUNT_IBAN, BigDecimal.TEN);
        accountService.transfer(fundsTransferRequest);
    }


}
