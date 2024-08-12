package com.example.fundTransferService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootTest
@EnableCaching
class FundTransferServiceApplicationTests {

    @Autowired
    private CacheManager cacheManager;

    @Test
    void contextLoads() {
    }

}
