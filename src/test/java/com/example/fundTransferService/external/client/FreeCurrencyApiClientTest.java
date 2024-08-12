package com.example.fundTransferService.external.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.fundTransferService.external.dto.FreeCurrencyApiResponse;

@SpringBootTest
public class FreeCurrencyApiClientTest {

    @Autowired
    FreeCurrencyApiClient freeCurrencyApiClient;

    @Autowired
    private CacheManager cacheManager;


    @BeforeEach
    public void before() throws Exception {
        freeCurrencyApiClient.clearGetLatestRatesCache();
        cacheManager.getCache("getLatestRates").clear();

    }

    @Test
    void getLatestRates_success_cacheIsPopulated() {
        FreeCurrencyApiResponse freeCurrencyApiResponse = new FreeCurrencyApiResponse();
        ResponseEntity<FreeCurrencyApiResponse> response = new ResponseEntity<FreeCurrencyApiResponse>(freeCurrencyApiResponse, HttpStatus.OK);

        assertEquals(0, ((Map) cacheManager.getCache("getLatestRates").getNativeCache()).size());
        freeCurrencyApiClient.getLatestRates();
        assertEquals(1, ((Map) cacheManager.getCache("getLatestRates").getNativeCache()).size());
        freeCurrencyApiClient.getLatestRates();

    }
}
