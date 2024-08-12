package com.rafaelwaldo.taxis.car.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.car.domain.Location;
import com.rafaelwaldo.taxis.car.domain.Taxi;
import com.rafaelwaldo.taxis.car.domain.TaxiCommand;
import com.rafaelwaldo.taxis.car.domain.TaxiCommandName;
import com.rafaelwaldo.taxis.car.domain.TaxiStatus;
import com.rafaelwaldo.taxis.car.domain.Trip;
import com.rafaelwaldo.taxis.car.domain.TripStatus;
import com.rafaelwaldo.taxis.car.domain.exception.CentralException;
import com.rafaelwaldo.taxis.car.domain.pojo.TaxiPojo;
import com.rafaelwaldo.taxis.car.infrastructure.TaxiCentralClient;
import com.rafaelwaldo.taxis.car.mapper.TaxiMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rafaelwaldo.taxis.car.utils.MockHelper.getMockTaxi;
import static com.rafaelwaldo.taxis.car.utils.MockHelper.getMockTrip;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private TaxiPojo taxiPojo;
    @Mock
    private ServiceLocation serviceLocation;
    @Mock
    private TaxiCentralClient taxiCentralClient;
    @Mock
    private TaxiMapper taxiMapper;
    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void completeCarTrip() {

        Trip trip = getMockTrip().build();
        Taxi taxi = getMockTaxi().build();

        when(taxiPojo.getCurrentTrip()).thenReturn(trip);
        when(taxiMapper.toDomain(taxiPojo)).thenReturn(taxi);

        Taxi result = carService.completeCarTrip();

        verify(taxiCentralClient).updateTripStatus(trip.uuid(), TripStatus.COMPLETED);
        verify(taxiPojo).setCurrentTrip(null);
        verify(taxiPojo).setTaxiStatus(TaxiStatus.AVAILABLE);
        assertEquals(taxi, result);
    }

    @Test
    void getTaxiInfo() {
        Taxi expectedTaxi = getMockTaxi().build();

        when(taxiMapper.toDomain(taxiPojo)).thenReturn(expectedTaxi);

        Taxi result = carService.getTaxiInfo();

        assertEquals(expectedTaxi, result);
        verify(taxiMapper).toDomain(taxiPojo);
    }

    @Test
    void publishTaxiCar() {
        Location location = new Location("foo", "bar");
        Taxi taxi = getMockTaxi().location(location).build();

        when(serviceLocation.getCurrentLocation()).thenReturn(location);
        when(taxiMapper.toDomain(taxiPojo)).thenReturn(taxi);
        when(taxiCentralClient.publishTaxis(taxi)).thenReturn(taxi);

        carService.publishTaxiCar();

        verify(taxiPojo).setLocation(location);
        verify(taxiCentralClient).publishTaxis(taxi);
        verify(taxiPojo).setUuid(taxi.uuid());
    }

    @Test
    void receiveTaxiCommand_publishTripCommand() throws JsonProcessingException {

        Taxi taxi = getMockTaxi().build();
        Trip trip = getMockTrip().taxi(taxi).build();
        TaxiCommand taxiCommand = new TaxiCommand(TaxiCommandName.PUBLISH_TRIP, objectMapper.writeValueAsString(trip));

        when(taxiPojo.getUuid()).thenReturn(taxi.uuid());
        when(taxiMapper.toDomain(taxiPojo)).thenReturn(taxi);

        carService.receiveTaxiCommand(objectMapper.writeValueAsString(taxiCommand));

        verify(taxiCentralClient).updateTripTaxi(trip.uuid(), taxi);
        verify(taxiPojo).setCurrentTrip(trip);
        verify(taxiPojo).setTaxiStatus(TaxiStatus.BOOKED);
    }

    @Test
    void receiveTaxiCommand_unknownCommand() {
        String message = "{ \"taxiCommandName\": \"UNKNOWN_COMMAND\", \"args\": [] }";

        carService.receiveTaxiCommand(message);

        verify(taxiCentralClient, never()).updateTripTaxi(any(), any());
        verify(taxiPojo, never()).setCurrentTrip(any());
        verify(taxiPojo, never()).setTaxiStatus(any());
    }


    @Test
    void receiveTaxiCommand_updateTripTaxiThrowsException() throws JsonProcessingException {

        Taxi taxi = getMockTaxi().build();
        Trip trip = getMockTrip().taxi(taxi).build();
        TaxiCommand taxiCommand = new TaxiCommand(TaxiCommandName.PUBLISH_TRIP, objectMapper.writeValueAsString(trip));

        when(taxiPojo.getUuid()).thenReturn(taxi.uuid());
        when(taxiMapper.toDomain(taxiPojo)).thenReturn(taxi);
        doThrow(new CentralException("Test Exception")).when(taxiCentralClient).updateTripTaxi(any(), any());

        carService.receiveTaxiCommand(objectMapper.writeValueAsString(taxiCommand));

        verify(taxiCentralClient).updateTripTaxi(any(), any());
        verify(taxiPojo, never()).setCurrentTrip(any());
        verify(taxiPojo, never()).setTaxiStatus(any());
    }
}