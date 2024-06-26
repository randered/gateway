package com.randered.Gateway.controller;

import com.randered.Gateway.model.json.request.JsonCurrentRequest;
import com.randered.Gateway.model.json.request.JsonHistoricalRequest;
import com.randered.Gateway.model.json.response.JsonResponse;
import com.randered.Gateway.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json_api")
public class JsonApiController {

    @Autowired
    private ResponseService service;

    @PostMapping("/current")
    public ResponseEntity<JsonResponse> getCurrentRates(@RequestBody JsonCurrentRequest jsonCurrentRequest) {
        return service.processJsonRequest(jsonCurrentRequest);
    }

    @PostMapping("/history")
    public ResponseEntity<JsonResponse> getHistoricalRates(@RequestBody JsonHistoricalRequest jsonHistoricalRequest) {
        return service.processJsonRequest(jsonHistoricalRequest);
    }
}
