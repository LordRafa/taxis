package com.rafaelwaldo.taxis.central.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQPublisherImpl implements RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(String exchangeName, String message) {
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }
}
