package com.example.fundTransferService.business.service;


import static com.example.fundTransferService.external.client.FreeCurrencyApiClient.API_NUMBER_OF_DECIMAL_PLACES;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.example.fundTransferService.exception.CurrencyNotSupportedException;
import com.example.fundTransferService.exception.UnableToRetrieveExchangeRateException;
import com.example.fundTransferService.external.client.FreeCurrencyApiClient;
import com.example.fundTransferService.external.dto.FreeCurrencyApiResponse;

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
    public BigDecimal getCurrentExchangeRate(String baseCurrency, String targetCurrency) {
        FreeCurrencyApiResponse freeCurrencyApiResponse = this.getLatestRates();
        Map<String, BigDecimal> currentRateMapping = freeCurrencyApiResponse.getCurrencyRateMap();
        if (currentRateMapping.containsKey(baseCurrency) && currentRateMapping.containsKey(targetCurrency)) {
            return currentRateMapping.get(targetCurrency).divide(currentRateMapping.get(baseCurrency), API_NUMBER_OF_DECIMAL_PLACES, RoundingMode.CEILING);
        } else {
            throw new CurrencyNotSupportedException(currentRateMapping.containsKey(baseCurrency) ? targetCurrency : baseCurrency);
        }
    }

    private FreeCurrencyApiResponse getLatestRates() {
        try {
            return freeCurrencyApiClient.getLatestRates();
        } catch (RestClientException restClientException) {
            throw new UnableToRetrieveExchangeRateException(restClientException.getMessage());
        }
    }

}
