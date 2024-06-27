package com.randered.Gateway.service.loader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.randered.Gateway.entity.Rate;
import com.randered.Gateway.exception.ApiRequestException;
import com.randered.Gateway.service.core.RatesService;
import com.randered.Gateway.service.cache.CacheCleanerService;
import com.randered.Gateway.service.core.impl.RatesServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Service
public class RatesDataLoaderService {

    private static final Logger logger = LoggerFactory.getLogger(RatesServiceImpl.class);

    @Autowired
    private RatesService ratesService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheCleanerService cleaner;

    @Value("${fixer.api.url}")
    private String apiUrl;

//    @Scheduled(fixedRate = 3600000)
    @Retryable(
            retryFor = {ApiRequestException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000))
    public void getCurrencyRates() {
        final Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

        if (response == null) {
            throw new ApiRequestException(404, "Received null response");
        }

        if (Boolean.FALSE.equals(response.get("success"))) {
            Map<String, Object> error = (Map<String, Object>) response.get("error");
            int errorCode = (int) error.get("code");
            String errorMessage = (String) error.get("info");
            throw new ApiRequestException(errorCode, errorMessage);
        }

        final Map<String, BigDecimal> rates = (Map<String, BigDecimal>) response.get("rates");
        final String baseCurrency = response.get("base").toString();

        try {
            saveRate(baseCurrency, rates);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        cleaner.cleanCache();
    }

    private void saveRate(final String baseCurrency, final Map<String, BigDecimal> rates) throws JsonProcessingException {
        final Rate rate = new Rate();
        rate.setBaseCurrency(baseCurrency);
        rate.setExchangeRates(setExchangeRates(rates));
        rate.setTimestamp(OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        ratesService.save(rate);
    }

    private String setExchangeRates(Map<String, BigDecimal> exchangeRates) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(exchangeRates);
    }
}
