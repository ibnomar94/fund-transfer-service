package com.example.fundTransferService.business.service;


import static com.example.fundTransferService.external.client.FreeCurrencyApiClient.API_NUMBER_OF_DECIMAL_PLACES;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.example.fundTransferService.business.domain.enums.Currency;
import com.example.fundTransferService.exception.CurrencyNotSupportedException;
import com.example.fundTransferService.exception.UnableToRetrieveExchangeRateException;
import com.example.fundTransferService.external.client.FreeCurrencyApiClient;
import com.example.fundTransferService.external.dto.FreeCurrencyApiResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class CurrencyConversionService {

    private final FreeCurrencyApiClient freeCurrencyApiClient;

    /**
     * The purpose of this method is to get the conversion 'rate'
     *
     * @param baseCurrency   the currency we want to change from
     * @param targetCurrency the currency we want to change to
     */
    public BigDecimal getCurrentExchangeRate(Currency baseCurrency, Currency targetCurrency) {
        FreeCurrencyApiResponse freeCurrencyApiResponse = this.getLatestRates();
        Map<Currency, BigDecimal> currentRateMapping = freeCurrencyApiResponse.getCurrencyRateMap();
        if (currentRateMapping.containsKey(baseCurrency) && currentRateMapping.containsKey(targetCurrency)) {
            // since the API returns exchange rates in comparison to USD we need to perform a 'cross multiplication' operation inorder to find the true exchange rate
            return currentRateMapping.get(targetCurrency).divide(currentRateMapping.get(baseCurrency), API_NUMBER_OF_DECIMAL_PLACES, RoundingMode.CEILING);
        } else {
            throw new CurrencyNotSupportedException(currentRateMapping.containsKey(baseCurrency) ? targetCurrency : baseCurrency);
        }
    }

    private FreeCurrencyApiResponse getLatestRates() {
        try {
            FreeCurrencyApiResponse freeCurrencyApiResponse = freeCurrencyApiClient.getLatestRates();
            log.info("Current Rates: " + freeCurrencyApiResponse.getCurrencyRateMap());
            return freeCurrencyApiResponse;
        } catch (RestClientException restClientException) {
            throw new UnableToRetrieveExchangeRateException(restClientException.getMessage());
        }
    }

}
