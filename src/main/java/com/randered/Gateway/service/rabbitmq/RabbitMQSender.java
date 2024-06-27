package com.randered.Gateway.service.rabbitmq;

import com.randered.Gateway.entity.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.randered.Gateway.config.RabbitMqConfig.QUEUE_NAME;

@Service
public class RabbitMQSender {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);

    @Value("${app.rabbitmq.exchange}")
    private String exchangeValue;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(final Request request) {
        rabbitTemplate.convertAndSend(exchangeValue, QUEUE_NAME, request);
        logger.info("Sending message to RabbitMQ" + request.getRequestId());
    }
}