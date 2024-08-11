package com.rafaelwaldo.taxis.central;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import static com.rafaelwaldo.taxis.central.utils.TestConstants.TAXI_COMMAND_EXCHANGE;
import static com.rafaelwaldo.taxis.central.utils.TestConstants.TAXI_COMMAND_FANOUT_QUEUE;

@TestConfiguration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitContainer() {
        return new RabbitMQContainer(DockerImageName.parse("rabbitmq:latest"));
    }

    @Bean
    public Declarables fanoutBindings() {

        FanoutExchange cmdExchange = new FanoutExchange(TAXI_COMMAND_EXCHANGE);
        Queue fanoutQueue1 = new Queue(TAXI_COMMAND_FANOUT_QUEUE, false);

        return new Declarables(
                fanoutQueue1,
                cmdExchange,
                BindingBuilder.bind(fanoutQueue1).to(cmdExchange));
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
