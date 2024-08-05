package com.example.fundTransferService.business.service;


import static com.example.fundTransferService.business.client.FreeCurrencyApiClient.API_NUMBER_OF_DECIMAL_PLACES;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.fundTransferService.business.client.FreeCurrencyApiClient;
import com.example.fundTransferService.business.dto.response.FreeCurrencyApiResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CurrencyConversionService {

    private final FreeCurrencyApiClient freeCurrencyApiClient;

    /**
     * The purpose of this method is to get the conversion 'rate'
     *
     * @param baseCurrency   the currency we want to change from
     * @param targetCurrency the currency we want to change to
     */
    public BigDecimal convertCurrency(String baseCurrency, String targetCurrency) {
        FreeCurrencyApiResponse freeCurrencyApiResponse = this.getLatestRates();
        Map<String, BigDecimal> currentRateMapping = freeCurrencyApiResponse.getCurrencyRateMap();
        if (currentRateMapping.containsKey(baseCurrency) && currentRateMapping.containsKey(targetCurrency)) {
            return currentRateMapping.get(targetCurrency).divide(currentRateMapping.get(baseCurrency), API_NUMBER_OF_DECIMAL_PLACES, RoundingMode.CEILING);
        } else {
            // TODO throw error
            throw new IllegalArgumentException();
        }
    }

    private FreeCurrencyApiResponse getLatestRates() {
        return freeCurrencyApiClient.getLatestRates();
    }

}
