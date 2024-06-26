package com.randered.Gateway.repository;

import com.randered.Gateway.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatesRepository extends JpaRepository<Rate, Long> {
    List<Rate> findAllByBaseCurrencyAndTimestampBetween(String baseCurrency, OffsetDateTime start, OffsetDateTime end);
    Optional<Rate> findFirstByBaseCurrencyOrderByTimestampDesc(String baseCurrency);
}
