package com.rafaelwaldo.taxis.central.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.central.domain.TaxiCommand;
import com.rafaelwaldo.taxis.central.domain.TaxiCommandName;
import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.infrastructure.RabbitMQPublisher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static com.rafaelwaldo.taxis.central.utils.MockHelper.getMockTrip;
import static com.rafaelwaldo.taxis.central.utils.TestConstants.TAXI_COMMAND_EXCHANGE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommandServiceImplTest {

    private static RabbitMQPublisher rabbitMQPublisher;

    private static CommandServiceImpl commandService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() throws NoSuchFieldException, IllegalAccessException {
        rabbitMQPublisher = Mockito.mock(RabbitMQPublisher.class);
        commandService = new CommandServiceImpl(rabbitMQPublisher);
        Field field = CommandServiceImpl.class.getDeclaredField("cmdExchangeName");
        field.setAccessible(true);
        field.set(commandService, TAXI_COMMAND_EXCHANGE);
    }

    @Test
    void sendPublishTripCommand() throws JsonProcessingException {

        Trip trip = getMockTrip().build();

        commandService.sendPublishTripCommand(trip);

        TaxiCommand expectedCommand = new TaxiCommand(TaxiCommandName.PUBLISH_TRIP, List.of(trip));
        String msg = objectMapper.writeValueAsString(expectedCommand);
        verify(rabbitMQPublisher, times(1)).sendMessage(TAXI_COMMAND_EXCHANGE, msg);
    }
}