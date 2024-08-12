package com.rafaelwaldo.taxis.central.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.central.config.TaxiCommandConfig;
import com.rafaelwaldo.taxis.central.domain.TaxiCommand;
import com.rafaelwaldo.taxis.central.domain.TaxiCommandName;
import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.domain.exception.CentralException;
import com.rafaelwaldo.taxis.central.infrastructure.RabbitMQPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final RabbitMQPublisher rabbitMQPublisher;
    private final TaxiCommandConfig taxiCommandConfig;

    @Override
    public void sendPublishTripCommand(Trip trip) {
        try {
            TaxiCommand taxiCommand = new TaxiCommand(TaxiCommandName.PUBLISH_TRIP, objectMapper.writeValueAsString(trip));
            String msg = objectMapper.writeValueAsString(taxiCommand);
            rabbitMQPublisher.sendMessage(taxiCommandConfig.getExchange(), msg);
        } catch (Exception e) {
            throw new CentralException("Error sending message to taxi command queue" + e.getMessage());
        }
    }

}
