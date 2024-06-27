package com.randered.Gateway.service.core.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.randered.Gateway.entity.Rate;
import com.randered.Gateway.model.json.request.BaseRequest;
import com.randered.Gateway.model.json.request.CurrentRequest;
import com.randered.Gateway.model.json.request.HistoricalRequest;
import com.randered.Gateway.model.json.response.JsonResponse;
import com.randered.Gateway.model.xml.request.XmlRequest;
import com.randered.Gateway.model.xml.response.XmlResponse;
import com.randered.Gateway.service.core.RatesService;
import com.randered.Gateway.service.core.RequestService;
import com.randered.Gateway.service.core.ResponseService;
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

    @Override
    public ResponseEntity<JsonResponse> processJsonRequest(final BaseRequest request) {
        if (requestService.existsByRequestId(request.getRequestId())) {
            return buildErrorJsonResponse("Duplicate request for id: " + request.getRequestId(), HttpStatus.CONFLICT);
        }
        if (request instanceof CurrentRequest) {
            return handleCurrentJsonRequest((CurrentRequest) request);
        } else if (request instanceof HistoricalRequest) {
            return handleHistoricalJsonRequest((HistoricalRequest) request);
        } else {
            return buildErrorJsonResponse("Invalid request type", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<XmlResponse> processXmlRequest(final String request) {
        XmlRequest data;

        try {
            XmlMapper xmlMapper = new XmlMapper();
            data = xmlMapper.readValue(request, XmlRequest.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            data = new XmlRequest();
            buildErrorXmlResponse("There was a problem processing your request", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (requestService.existsByRequestId(data.getId())) {
            return buildErrorXmlResponse("Duplicate request for id: " + data.getId(), HttpStatus.CONFLICT);
        }

        if (data.getGet() != null) {
            return handleCurrentXmlCommand(data);
        } else if (data.getHistory() != null) {
            return handleHistoryXmlCommand(data);
        } else {
            buildErrorXmlResponse("Invalid request type", HttpStatus.BAD_REQUEST);
        }
        XmlResponse response = new XmlResponse();
        response.setMessage("Error processing request");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<XmlResponse> handleCurrentXmlCommand(final XmlRequest data) {
        final String baseCurrency = data.getGet().getCurrency();
        final Rate currentRate = ratesService.getCurrentRate(baseCurrency);
        if (currentRate != null) {
            return buildSuccessXmlResponse(data.getId(), baseCurrency, data.getGet().getConsumer(), List.of(currentRate));
        } else {
            return buildErrorXmlResponse("Currency data not found: " + baseCurrency, HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<XmlResponse> handleHistoryXmlCommand(final XmlRequest data) {
        final String baseCurrency = data.getHistory().getCurrency();
        final List<Rate> historicalRates;

        if (data.getHistory().getPeriod() == 24) {
            historicalRates = ratesService.getLast24hOfRecords(baseCurrency);
        } else {
            historicalRates = ratesService.getHistoricalRates(baseCurrency, data.getHistory().getPeriod());
        }

        if (historicalRates != null) {
            return buildSuccessXmlResponse(data.getId(), data.getHistory().getConsumer(), baseCurrency, historicalRates);
        } else {
            return buildErrorXmlResponse("Currency data not found: " + baseCurrency, HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<XmlResponse> buildErrorXmlResponse(final String message, final HttpStatus status) {
        XmlResponse response = new XmlResponse();
        response.setMessage(message);
        return new ResponseEntity<>(response, status);
    }

    private ResponseEntity<XmlResponse> buildSuccessXmlResponse(
            final String requestId,
            final String baseCurrency,
            final String clientId,
            final List<Rate> ratesData) {
        XmlResponse response = new XmlResponse();
        response.setMessage("Success");
        response.setBaseCurrency(baseCurrency);
        response.setExchangeRates(ratesData);
        requestService.saveXmlRequest(requestId, clientId, "EXT_SERVICE_1");
        logger.info("Sent data for request: " + requestId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<JsonResponse> handleCurrentJsonRequest(final CurrentRequest request) {
        final Rate currentRate = ratesService.getCurrentRate(request.getCurrency());
        if (currentRate != null) {
            return buildSuccessJsonResponse(request, List.of(currentRate));
        } else {
            return buildErrorJsonResponse("Currency data not found: " + request.getCurrency(), HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<JsonResponse> handleHistoricalJsonRequest(final HistoricalRequest request) {
        final List<Rate> historicalRates;

        if (request.getPeriod() == 24) {
            historicalRates = ratesService.getLast24hOfRecords(request.getCurrency());
        } else {
            historicalRates = ratesService.getHistoricalRates(request.getCurrency(), request.getPeriod());
        }

        if (!historicalRates.isEmpty()) {
            return buildSuccessJsonResponse(request, historicalRates);
        } else {
            return buildErrorJsonResponse("History for currency not found: " + request.getCurrency(), HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<JsonResponse> buildSuccessJsonResponse(final BaseRequest request, final List<Rate> ratesData) {
        JsonResponse jsonResponse = JsonResponse.builder()
                .baseCurrency(request.getCurrency())
                .message("Success")
                .exchangeRates(ratesData)
                .build();
        requestService.saveRequest(request, "EXT_SERVICE_2");
        logger.info("Sent data for request: " + request.getRequestId());
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    private ResponseEntity<JsonResponse> buildErrorJsonResponse(final String message, final HttpStatus status) {
        JsonResponse jsonResponse = JsonResponse.builder().message(message).build();
        return new ResponseEntity<>(jsonResponse, status);
    }
}
