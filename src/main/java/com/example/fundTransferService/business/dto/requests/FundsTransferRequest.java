package com.example.fundTransferService.business.dto.requests;


import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class FundsTransferRequest {
    @NotBlank
    String accountToDebitIban;
    @NotBlank
    String accountToCreditIban;
    @NotNull
    @Positive
    BigDecimal amount;
}
