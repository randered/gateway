package com.randered.Gateway.service.impl;

import com.randered.Gateway.entity.Rate;
import com.randered.Gateway.model.json.request.BaseJsonRequest;
import com.randered.Gateway.model.json.request.JsonCurrentRequest;
import com.randered.Gateway.model.json.request.JsonHistoricalRequest;
import com.randered.Gateway.model.json.response.JsonResponse;
import com.randered.Gateway.service.RatesService;
import com.randered.Gateway.service.RequestService;
import com.randered.Gateway.service.ResponseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {
    private static Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);

    @Autowired
    private RatesService ratesService;
    @Autowired
    private RequestService requestService;

//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//    @Value("${app.rabbitmq.exchange}")
//    private String exchange;

    public ResponseEntity<JsonResponse> processJsonRequest(final BaseJsonRequest request) {
        if (requestService.existsByRequestId(request.getRequestId())) {
            return buildErrorResponse("Duplicate request for id: " + request.getRequestId(), HttpStatus.CONFLICT);
        }
        if (request instanceof JsonCurrentRequest) {
            return handleCurrentRequest((JsonCurrentRequest) request);
        } else if (request instanceof JsonHistoricalRequest) {
            return handleHistoricalRequest((JsonHistoricalRequest) request);
        } else {
            return buildErrorResponse("Invalid request type", HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<JsonResponse> handleCurrentRequest(final JsonCurrentRequest request) {
        final Rate currentRate = ratesService.getCurrentRate(request.getCurrency());
        if (currentRate != null) {
            return buildSuccessResponse(request, List.of(currentRate));
        } else {
            return buildErrorResponse("Currency data not found: " + request.getCurrency(), HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<JsonResponse> handleHistoricalRequest(final JsonHistoricalRequest request) {
        final List<Rate> historicalRates;

        if (request.getPeriod() == 24) {
            historicalRates = ratesService.getLast24hOfRecords(request.getCurrency());
        } else {
            historicalRates = ratesService.getHistoricalRates(request.getCurrency(), request.getPeriod());
        }

        if (!historicalRates.isEmpty()) {
            return buildSuccessResponse(request, historicalRates);
        } else {
            return buildErrorResponse("History for currency not found: " + request.getCurrency(), HttpStatus.NOT_FOUND);
        }
    }


    private ResponseEntity<JsonResponse> buildSuccessResponse(final BaseJsonRequest request, final List<Rate> ratesData) {
        JsonResponse jsonResponse = JsonResponse.builder()
                .baseCurrency(request.getCurrency())
                .message("Success")
                .exchangeRates(ratesData)
                .build();
        requestService.saveRequest(request, "Json-Service");
        logger.info("Sent data for request: " + request.getRequestId());
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    private ResponseEntity<JsonResponse> buildErrorResponse(final String message, final HttpStatus status) {
        JsonResponse jsonResponse = JsonResponse.builder().message(message).build();
        return new ResponseEntity<>(jsonResponse, status);
    }
}
