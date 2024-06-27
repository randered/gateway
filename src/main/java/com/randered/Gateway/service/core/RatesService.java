package com.randered.Gateway.service.core;

import com.randered.Gateway.entity.Rate;

import java.util.List;


public interface RatesService {

    Rate save(Rate rate);

    Rate getCurrentRate(String currency);

    List<Rate> getHistoricalRates(String currency, Integer hours);

    List<Rate> getLast24hOfRecords(String currency);

}
