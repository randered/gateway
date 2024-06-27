package com.randered.Gateway.service.core.impl;

import com.randered.Gateway.entity.Request;
import com.randered.Gateway.model.json.request.BaseRequest;
import com.randered.Gateway.repository.RequestRepository;
import com.randered.Gateway.service.core.RequestService;
import com.randered.Gateway.service.rabbitmq.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestRepository repository;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    public boolean existsByRequestId(String requestId) {
        return repository.existsByRequestId(requestId);
    }

    public Request saveRequest(final BaseRequest request, final String serviceName) {
        Request requestLog = new Request();
        requestLog.setRequestId(request.getRequestId());
        requestLog.setClientId(request.getClient());
        requestLog.setServiceName(serviceName);
        requestLog.setTimestamp(OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        rabbitMQSender.send(requestLog);
        return repository.save(requestLog);
    }

    public Request saveXmlRequest(final String requestId, final String clientId, final String serviceName) {
        Request requestLog = new Request();
        requestLog.setRequestId(requestId);
        requestLog.setClientId(clientId);
        requestLog.setServiceName(serviceName);
        requestLog.setTimestamp(OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        rabbitMQSender.send(requestLog);
        return repository.save(requestLog);
    }
}
