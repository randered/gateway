package com.randered.Gateway.service;

import com.randered.Gateway.model.json.request.BaseJsonRequest;
import com.randered.Gateway.model.json.response.JsonResponse;
import org.springframework.http.ResponseEntity;

public interface ResponseService {
    ResponseEntity<JsonResponse> processJsonRequest(BaseJsonRequest currentRequest);
}
