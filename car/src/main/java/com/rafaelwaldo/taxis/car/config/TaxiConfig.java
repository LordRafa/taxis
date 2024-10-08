package com.rafaelwaldo.taxis.car.config;

import com.rafaelwaldo.taxis.car.domain.TaxiStatus;
import com.rafaelwaldo.taxis.car.domain.pojo.TaxiPojo;
import lombok.Data;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "taxi")
@Data
@Configuration
public class TaxiConfig {

    private String plate;
    private Command command;

    @Bean
    public Declarables fanoutBindings() {

        FanoutExchange commandExchange = new FanoutExchange(command.getExchange());
        Queue fanoutQueue1 = new Queue(command.queue, false);

        return new Declarables(
                fanoutQueue1,
                commandExchange,
                BindingBuilder.bind(fanoutQueue1).to(commandExchange));
    }

    @Bean
    public TaxiPojo taxiPojo() {
        return TaxiPojo.builder()
                .plate(plate)
                .taxiStatus(TaxiStatus.AVAILABLE)
                .build();
    }

    @Data
    public static class Command {
        private String exchange;
        private String queue;
    }
}
