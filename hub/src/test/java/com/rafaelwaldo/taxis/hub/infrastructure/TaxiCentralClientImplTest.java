package com.rafaelwaldo.taxis.hub.infrastructure;

import com.rafaelwaldo.taxis.hub.domain.Taxi;
import com.rafaelwaldo.taxis.hub.domain.Trip;
import com.rafaelwaldo.taxis.hub.domain.TripStatus;
import com.rafaelwaldo.taxis.hub.domain.exception.CentralException;
import com.rafaelwaldo.taxis.hub.domain.exception.TripNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static com.rafaelwaldo.taxis.hub.utils.MockHelper.getMockTaxi;
import static com.rafaelwaldo.taxis.hub.utils.MockHelper.getMockTrip;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaxiCentralClientImplTest {

    private static final String TAXI_CENTRAL_HOST = "http://localhost:8080";
    private static RestTemplate restTemplate;
    private static TaxiCentralClientImpl taxiCentralClient;

    @BeforeAll
    public static void setUp() throws NoSuchFieldException, IllegalAccessException {
        restTemplate = Mockito.mock(RestTemplate.class);
        taxiCentralClient = new TaxiCentralClientImpl(restTemplate);
        Field field = TaxiCentralClientImpl.class.getDeclaredField("taxiCentralHost");
        field.setAccessible(true);
        field.set(taxiCentralClient, TAXI_CENTRAL_HOST);
    }


    @Test
    void testListTaxis() {
        Taxi taxi = getMockTaxi().build();
        when(restTemplate.getForEntity(TAXI_CENTRAL_HOST + "/central/taxi", Taxi[].class)).thenReturn(new ResponseEntity<>(new Taxi[]{taxi}, HttpStatus.OK));

        List<Taxi> taxis = taxiCentralClient.listTaxis();

        assertEquals(1, taxis.size());
        assertEquals(taxi, taxis.get(0));
    }

    @Test
    void testListTaxisFailure() {
        when(restTemplate.getForEntity(TAXI_CENTRAL_HOST + "/central/taxi", Taxi[].class))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(CentralException.class, () -> taxiCentralClient.listTaxis());
    }

    @Test
    void testPublishTrip() {
        Trip trip = getMockTrip().build();
        when(restTemplate.postForEntity(TAXI_CENTRAL_HOST + "/central/trip", trip, Trip.class))
                .thenReturn(new ResponseEntity<>(trip, HttpStatus.OK));

        Trip result = taxiCentralClient.publishTrip(trip);

        assertEquals(trip, result);
    }

    @Test
    void testPublishTripFailure() {
        Trip trip = getMockTrip().build();
        when(restTemplate.postForEntity(TAXI_CENTRAL_HOST + "/central/trip", trip, Trip.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(CentralException.class, () -> taxiCentralClient.publishTrip(trip));
    }

    @Test
    void testGetAssignedTripTaxi() {
        UUID tripUuid = UUID.randomUUID();
        Taxi taxi = getMockTaxi().build();
        when(restTemplate.getForEntity(TAXI_CENTRAL_HOST + "/central/trip/" + tripUuid + "/taxi", Taxi.class))
                .thenReturn(new ResponseEntity<>(taxi, HttpStatus.OK));

        Taxi result = taxiCentralClient.getAssignedTripTaxi(tripUuid);

        assertEquals(taxi, result);
    }

    @Test
    void testGetAssignedTripTaxiNotFound() {
        UUID tripUuid = UUID.randomUUID();
        when(restTemplate.getForEntity(TAXI_CENTRAL_HOST + "/central/trip/" + tripUuid + "/taxi", Taxi.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        assertThrows(TripNotFoundException.class, () -> taxiCentralClient.getAssignedTripTaxi(tripUuid));
    }

    @Test
    void testGetAssignedTripTaxiFailure() {
        UUID tripUuid = UUID.randomUUID();
        when(restTemplate.getForEntity(TAXI_CENTRAL_HOST + "/central/trip/" + tripUuid + "/taxi", Taxi.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(CentralException.class, () -> taxiCentralClient.getAssignedTripTaxi(tripUuid));
    }

    @Test
    void testCancelTrip() {
        UUID tripUuid = UUID.randomUUID();
        RequestEntity<TripStatus> requestEntity = RequestEntity
                .put(TAXI_CENTRAL_HOST + "/central/trip/" + tripUuid + "/tripStatus")
                .body(TripStatus.CANCELED);
        when(restTemplate.exchange(requestEntity, Trip.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        taxiCentralClient.cancelTrip(tripUuid);

        Mockito.verify(restTemplate).exchange(requestEntity, Trip.class);

    }
}