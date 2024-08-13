package com.example.fundTransferService.external.dto;

import java.math.BigDecimal;
import java.util.HashMap;

import com.example.fundTransferService.business.domain.enums.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@JsonSerialize
@Setter
@Getter
public class FreeCurrencyApiResponse {

    @JsonProperty("data")
    HashMap<Currency, BigDecimal> currencyRateMap;
}
