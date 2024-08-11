package com.rafaelwaldo.taxis.central.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TAXI_COMMAND_EXCHANGE = "taxiCommandExchange";

    @Bean
    public FanoutExchange cmdExchange() {
        return new FanoutExchange(TAXI_COMMAND_EXCHANGE);
    }

}
