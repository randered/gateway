package com.randered.Gateway.service.core;

import com.randered.Gateway.entity.Request;
import com.randered.Gateway.model.json.request.BaseRequest;

public interface RequestService {
    boolean existsByRequestId(String requestId);
    Request saveRequest(BaseRequest request, String serviceName);

    Request saveXmlRequest(String requestId, String clientId, String serviceName);

}
