package com.example.fundTransferService.business.dto.response;

import java.math.BigDecimal;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@JsonSerialize
@Setter
@Getter
public class FreeCurrencyApiResponse {

    @JsonProperty("data")
    HashMap<String, BigDecimal> currencyRateMap;
}
