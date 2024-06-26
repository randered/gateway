package com.randered.Gateway.service.impl;

import com.randered.Gateway.entity.Request;
import com.randered.Gateway.model.json.request.BaseJsonRequest;
import com.randered.Gateway.repository.RequestRepository;
import com.randered.Gateway.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestRepository repository;

    @Override
    public boolean existsByRequestId(String requestId) {
        return repository.existsByRequestId(requestId);
    }

    public Request saveRequest(final BaseJsonRequest request, final String serviceName) {
        Request requestLog = new Request();
        requestLog.setRequestId(request.getRequestId());
        requestLog.setClientId(request.getClient());
        requestLog.setServiceName(serviceName);
        requestLog.setTimestamp(OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        return repository.save(requestLog);
    }
}
