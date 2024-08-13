package com.example.fundTransferService.integration.business.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.example.fundTransferService.TestDataUtils.generateFundsTransferRequest;
import static com.example.fundTransferService.TestDataUtils.generateTestAccount;
import static com.example.fundTransferService.TestDataUtils.generateTestAccountHolder;
import static com.example.fundTransferService.TestDataUtils.getRequestBodyJson;
import static com.example.fundTransferService.integration.business.service.AccountServiceIT.CREDIT_ACCOUNT_IBAN;
import static com.example.fundTransferService.integration.business.service.AccountServiceIT.CREDIT_ACCOUNT_USD_IBAN;
import static com.example.fundTransferService.integration.business.service.AccountServiceIT.DEBIT_ACCOUNT_IBAN;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.fundTransferService.business.domain.enums.Currency;
import com.example.fundTransferService.business.dto.requests.FundsTransferRequest;
import com.example.fundTransferService.business.model.Account;
import com.example.fundTransferService.business.model.AccountHolder;
import com.example.fundTransferService.business.respository.AccountHolderRepository;
import com.example.fundTransferService.business.respository.AccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountControllerIT {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @BeforeAll
    public void init() {
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

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnTransactionID() throws Exception {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest(DEBIT_ACCOUNT_IBAN, CREDIT_ACCOUNT_IBAN, BigDecimal.TEN);

        this.mockMvc.perform(post("/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBodyJson(fundsTransferRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionId").exists());

    }

    @Test
    void shouldReturnExceptionFormatted() throws Exception {
        FundsTransferRequest fundsTransferRequest = generateFundsTransferRequest(DEBIT_ACCOUNT_IBAN, CREDIT_ACCOUNT_IBAN, BigDecimal.valueOf(250));

        this.mockMvc.perform(post("/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequestBodyJson(fundsTransferRequest)))
                .andDo(print())
                .andExpect(status().is(422))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Account with IBAN: " + DEBIT_ACCOUNT_IBAN + " does not have sufficient balance to execute this transaction"));

    }
}
