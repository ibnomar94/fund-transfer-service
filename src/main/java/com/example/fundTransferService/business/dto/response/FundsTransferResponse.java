package com.example.fundTransferService.business.dto.response;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FundsTransferResponse {
    String transactionId;
    BigDecimal changeRate;
}
