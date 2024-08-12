package com.rafaelwaldo.taxis.car.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.car.domain.Taxi;
import com.rafaelwaldo.taxis.car.domain.TaxiCommand;
import com.rafaelwaldo.taxis.car.domain.TaxiCommandName;
import com.rafaelwaldo.taxis.car.domain.TaxiStatus;
import com.rafaelwaldo.taxis.car.domain.Trip;
import com.rafaelwaldo.taxis.car.domain.TripStatus;
import com.rafaelwaldo.taxis.car.domain.pojo.TaxiPojo;
import com.rafaelwaldo.taxis.car.infrastructure.TaxiCentralClient;
import com.rafaelwaldo.taxis.car.mapper.TaxiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final TaxiPojo taxiPojo;
    private final ServiceLocation serviceLocation;
    private final TaxiCentralClient taxiCentralClient;
    private final TaxiMapper taxiMapper;

    @Override
    public Taxi completeCarTrip() {
        try {
            if (taxiPojo.getCurrentTrip() != null) {
                taxiCentralClient.updateTripStatus(taxiPojo.getCurrentTrip().uuid(), TripStatus.COMPLETED);
            }
            taxiPojo.setCurrentTrip(null);
            taxiPojo.setTaxiStatus(TaxiStatus.AVAILABLE);
        } catch (Exception e) {
            log.error("Error completing trip", e);
        }
        return taxiMapper.toDomain(taxiPojo);
    }

    @Override
    public Taxi getTaxiInfo() {
        return taxiMapper.toDomain(taxiPojo);
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void publishTaxiCar() {
        try {
            taxiPojo.setLocation(serviceLocation.getCurrentLocation());
            Taxi taxi = taxiCentralClient.publishTaxis(taxiMapper.toDomain(taxiPojo));
            taxiPojo.setUuid(taxi.uuid());
        } catch (Exception e) {
            log.error("Error publishing taxi", e);
        }
    }

    @RabbitListener(queues = "${taxi.command.queue}")
    public void receiveTaxiCommand(String message) {
        try {

            if (taxiPojo.getUuid() == null) {
                log.warn("Taxi not published yet");
                return;
            }

            TaxiCommand taxiCommand = objectMapper.readValue(message, TaxiCommand.class);

            if (Objects.requireNonNull(taxiCommand.taxiCommandName()) == TaxiCommandName.PUBLISH_TRIP) {
                Trip trip = objectMapper.readValue(taxiCommand.args(), Trip.class);
                taxiCentralClient.updateTripTaxi(trip.uuid(), taxiMapper.toDomain(taxiPojo));
                taxiPojo.setCurrentTrip(trip);
                taxiPojo.setTaxiStatus(TaxiStatus.BOOKED);
            } else {
                log.error("Unknown command: " + taxiCommand.taxiCommandName());
            }
        } catch (JsonProcessingException e) {
            log.error("Unknown command: " + message, e);
        } catch (Exception e) {
            log.error("Error processing command", e);
        }
    }

}
