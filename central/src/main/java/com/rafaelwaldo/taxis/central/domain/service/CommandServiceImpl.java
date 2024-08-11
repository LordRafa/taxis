package com.rafaelwaldo.taxis.central.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.central.domain.TaxiCommand;
import com.rafaelwaldo.taxis.central.domain.TaxiCommandName;
import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.domain.exception.CentralException;
import com.rafaelwaldo.taxis.central.infrastructure.RabbitMQPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final RabbitMQPublisher rabbitMQPublisher;

    @Value("${taxi.command.exchange}")
    private String cmdExchangeName;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void sendPublishTripCommand(Trip trip) {
        TaxiCommand taxiCommand = new TaxiCommand(TaxiCommandName.PUBLISH_TRIP, List.of(trip));
        try {
            String msg = objectMapper.writeValueAsString(taxiCommand);
            rabbitMQPublisher.sendMessage(cmdExchangeName, msg);
        } catch (Exception e) {
            throw new CentralException("Error sending message to taxi command queue" + e.getMessage());
        }
    }

}
