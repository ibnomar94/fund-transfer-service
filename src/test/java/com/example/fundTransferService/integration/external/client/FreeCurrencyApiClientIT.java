package com.example.fundTransferService.integration.external.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import com.example.fundTransferService.external.client.FreeCurrencyApiClient;

@SpringBootTest
public class FreeCurrencyApiClientIT {

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
        assertEquals(0, ((Map) cacheManager.getCache("getLatestRates").getNativeCache()).size());
        freeCurrencyApiClient.getLatestRates();
        assertEquals(1, ((Map) cacheManager.getCache("getLatestRates").getNativeCache()).size());
    }
}
