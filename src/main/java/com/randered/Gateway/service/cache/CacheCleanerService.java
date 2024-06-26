package com.randered.Gateway.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CacheCleanerService {
    private static final Logger logger = LoggerFactory.getLogger(CacheCleanerService.class);

    @CacheEvict(value = {"cached24hRates", "latestRates"}, allEntries = true)
    public void cleanCache(){
        logger.info("Cleaning the cached values.");
    }
}
