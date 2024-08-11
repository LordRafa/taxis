package com.rafaelwaldo.taxis.central.infrastructure;

public interface RabbitMQPublisher {
    void sendMessage(String queueName, String message);
}
