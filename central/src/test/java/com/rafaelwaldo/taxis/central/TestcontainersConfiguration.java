package com.rafaelwaldo.taxis.central;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class TestcontainersConfiguration {

    public static final String TAXI_COMMAND_FANOUT_QUEUE = "taxiCommandFanout.queue";

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitContainer() {
        return new RabbitMQContainer(DockerImageName.parse("rabbitmq:latest"));
    }

    @Bean
    public Declarables fanoutBindings(@Autowired FanoutExchange commandExchange) {

        Queue fanoutQueue = new Queue(TAXI_COMMAND_FANOUT_QUEUE, false);

        return new Declarables(
                fanoutQueue,
                commandExchange,
                BindingBuilder.bind(fanoutQueue).to(commandExchange));
    }

    @Bean
    @ServiceConnection
    MariaDBContainer<?> mariaDBContainer() {
        return new MariaDBContainer<>(DockerImageName.parse("mariadb:latest"))
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
    }

}
