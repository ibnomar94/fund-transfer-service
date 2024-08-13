package com.example.fundTransferService.unit.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fundTransferService.business.domain.Currency;
import com.example.fundTransferService.business.service.CurrencyConversionService;
import com.example.fundTransferService.exception.CurrencyNotSupportedException;
import com.example.fundTransferService.external.client.FreeCurrencyApiClient;
import com.example.fundTransferService.external.dto.FreeCurrencyApiResponse;

@ExtendWith(MockitoExtension.class)
public class CurrencyConversionServiceTest {

    @Mock
    private FreeCurrencyApiClient freeCurrencyApiClient;

    @InjectMocks
    private CurrencyConversionService currencyConversionService;

    @Test
    @DisplayName("Testing that currency exchange rate is retrieved correctly and with the expected precision")
    void getCurrentExchangeRate_validCurrencies_validConversionRateReturned() {
        FreeCurrencyApiResponse freeCurrencyApiResponse = new FreeCurrencyApiResponse();
        HashMap<Currency, BigDecimal> currencyRateMap = new HashMap<>();
        currencyRateMap.put(Currency.EUR, BigDecimal.valueOf(0.59));
        currencyRateMap.put(Currency.AUD, BigDecimal.valueOf(2.45));
        freeCurrencyApiResponse.setCurrencyRateMap(currencyRateMap);
        when(freeCurrencyApiClient.getLatestRates()).thenReturn(freeCurrencyApiResponse);

        BigDecimal exchangeRate = currencyConversionService.getCurrentExchangeRate(Currency.EUR, Currency.AUD);
        BigDecimal expected = new BigDecimal("4.1525423729");
        assertEquals(exchangeRate, expected);
    }

    @Test
    @DisplayName("Testing that non supported currencies throw expected exception")
    void getCurrentExchangeRate_invalidCurrencies_exceptionThrown() {
        FreeCurrencyApiResponse freeCurrencyApiResponse = new FreeCurrencyApiResponse();
        HashMap<Currency, BigDecimal> currencyRateMap = new HashMap<>();
        currencyRateMap.put(Currency.EUR, BigDecimal.valueOf(0.59));
        currencyRateMap.put(Currency.AUD, BigDecimal.valueOf(2.45));
        freeCurrencyApiResponse.setCurrencyRateMap(currencyRateMap);
        when(freeCurrencyApiClient.getLatestRates()).thenReturn(freeCurrencyApiResponse);

        Exception exception = assertThrows(CurrencyNotSupportedException.class, () -> {
            currencyConversionService.getCurrentExchangeRate(Currency.EUR, Currency.CHF);
        });

        assertEquals(exception.getMessage(), Currency.CHF.toString());
    }
}

