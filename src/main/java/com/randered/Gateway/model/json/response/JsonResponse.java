package com.randered.Gateway.model.json.response;

import com.randered.Gateway.entity.Rate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JsonResponse {
    private String message;
    private String baseCurrency;
    private List<Rate> exchangeRates;
}
