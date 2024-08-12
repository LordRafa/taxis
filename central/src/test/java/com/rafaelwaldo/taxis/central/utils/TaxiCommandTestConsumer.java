package com.rafaelwaldo.taxis.central.utils;

import lombok.Getter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

import static com.rafaelwaldo.taxis.central.utils.TestConstants.TAXI_COMMAND_FANOUT_QUEUE;

@Getter
@Component
public class TaxiCommandTestConsumer {

    private final CountDownLatch latch = new CountDownLatch(1);
    private String receivedMessage;

    @RabbitListener(queues = TAXI_COMMAND_FANOUT_QUEUE)
    public void receiveMessage(String message) {
        this.receivedMessage = message;
        latch.countDown();
    }

}
