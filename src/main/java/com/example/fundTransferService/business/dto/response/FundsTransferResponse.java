package com.example.fundTransferService.business.dto.response;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class FundsTransferResponse {
    String transactionId;
    BigDecimal exchangeRate;
    BigDecimal exchangeFee;
}
