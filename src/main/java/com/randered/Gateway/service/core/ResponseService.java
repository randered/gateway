package com.randered.Gateway.service.core;

import com.randered.Gateway.model.json.request.BaseRequest;
import com.randered.Gateway.model.json.response.JsonResponse;
import com.randered.Gateway.model.xml.response.XmlResponse;
import org.springframework.http.ResponseEntity;

public interface ResponseService {
    ResponseEntity<JsonResponse> processJsonRequest(BaseRequest currentRequest);

    ResponseEntity<XmlResponse> processXmlRequest(String request);
}
