package com.rafaelwaldo.taxis.central.config;

import lombok.Data;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "taxi.command")
@Data
@Configuration
public class TaxiCommandConfig {

    private String exchange;

    @Bean
    public FanoutExchange commandExchange() {
        return new FanoutExchange(exchange);
    }

}
