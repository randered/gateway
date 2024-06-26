package com.randered.Gateway.service;

import com.randered.Gateway.entity.Request;
import com.randered.Gateway.model.json.request.BaseJsonRequest;

public interface RequestService {
    boolean existsByRequestId(String requestId);
    Request saveRequest(BaseJsonRequest request, String serviceName);
}
