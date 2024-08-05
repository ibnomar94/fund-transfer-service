package com.example.fundTransferService.business.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.fundTransferService.business.dto.response.FreeCurrencyApiResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FreeCurrencyApiClient {

    public static int API_NUMBER_OF_DECIMAL_PLACES = 10;

    private final RestTemplate restTemplate;

    @Value("${clients.freeCurrencyApi.api-key}")
    private String API_KEY;

    @Value("${clients.freeCurrencyApi.base-url}")
    private String BASE_URL;

    private String LATEST_CONVERSION_ENDPOINT = "/v1/latest";

    /**
     * The purpose of this method is to get the conversion 'rate' oc currencies based on USD
     */
    public FreeCurrencyApiResponse getLatestRates() {

        return restTemplate.exchange(
                getLatestUrl(),
                HttpMethod.GET,
                null,
                FreeCurrencyApiResponse.class).getBody();
    }

    private String getLatestUrl() {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL + LATEST_CONVERSION_ENDPOINT)
                .queryParam("apikey", API_KEY)
                .toUriString();
    }

}
