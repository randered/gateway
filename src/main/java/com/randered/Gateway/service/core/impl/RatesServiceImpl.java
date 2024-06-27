package com.randered.Gateway.service.core.impl;

import com.randered.Gateway.entity.Rate;
import com.randered.Gateway.repository.RatesRepository;
import com.randered.Gateway.service.core.RatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class RatesServiceImpl implements RatesService {
    private static final Logger logger = LoggerFactory.getLogger(RatesServiceImpl.class);

    @Autowired
    private RatesRepository repository;

    @Override
    public Rate save(Rate rate) {
        return repository.save(rate);
    }

    @Cacheable(value = "latestRates", key = "#currency")
    public Rate getCurrentRate(final String currency) {
        logger.info("Fetching latest rates for currency: {} from database", currency);
        return repository.findFirstByBaseCurrencyOrderByTimestampDesc(currency).orElse(null);
    }

    public List<Rate> getHistoricalRates(final String currency, final Integer hours) {
        logger.info("Fetching historical rates for currency: {} from database", currency);
        OffsetDateTime end = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        OffsetDateTime start = end.minusHours(hours);
        return repository.findAllByBaseCurrencyAndTimestampBetween(currency, start, end);
    }

    @Cacheable(value = "cached24hRates", key = "#currency")
    public List<Rate> getLast24hOfRecords(final String currency) {
        OffsetDateTime end = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        OffsetDateTime start = end.minusHours(24);
        return repository.findAllByBaseCurrencyAndTimestampBetween(currency, start, end);
    }
}
