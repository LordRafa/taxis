package com.rafaelwaldo.taxis.central.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.central.config.TaxiCommandConfig;
import com.rafaelwaldo.taxis.central.domain.TaxiCommand;
import com.rafaelwaldo.taxis.central.domain.TaxiCommandName;
import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.infrastructure.RabbitMQPublisher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rafaelwaldo.taxis.central.utils.MockHelper.getMockTrip;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommandServiceImplTest {

    private static final String TAXI_COMMAND_EXCHANGE = "taxiCommandExchange";
    private static RabbitMQPublisher rabbitMQPublisher;

    private static CommandServiceImpl commandService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {
        rabbitMQPublisher = Mockito.mock(RabbitMQPublisher.class);
        TaxiCommandConfig taxiCommandConfig = new TaxiCommandConfig();
        taxiCommandConfig.setExchange(TAXI_COMMAND_EXCHANGE);
        commandService = new CommandServiceImpl(rabbitMQPublisher, taxiCommandConfig);
    }

    @Test
    void sendPublishTripCommand() throws JsonProcessingException {

        Trip trip = getMockTrip().build();

        commandService.sendPublishTripCommand(trip);

        TaxiCommand expectedCommand = new TaxiCommand(TaxiCommandName.PUBLISH_TRIP, objectMapper.writeValueAsString(trip));
        String msg = objectMapper.writeValueAsString(expectedCommand);
        verify(rabbitMQPublisher, times(1)).sendMessage(TAXI_COMMAND_EXCHANGE, msg);
    }
}