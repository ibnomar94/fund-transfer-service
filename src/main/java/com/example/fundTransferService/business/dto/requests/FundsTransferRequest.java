package com.example.fundTransferService.business.dto.requests;


import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class FundsTransferRequest {
    String accountToDebitIban;
    String accountToCreditIban;
    BigDecimal amount;
}
