package com.rafaelwaldo.taxis.car.infrastructure;

import com.rafaelwaldo.taxis.car.domain.Taxi;
import com.rafaelwaldo.taxis.car.domain.Trip;
import com.rafaelwaldo.taxis.car.domain.TripStatus;
import com.rafaelwaldo.taxis.car.domain.exception.CentralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.UUID;

import static com.rafaelwaldo.taxis.car.util.CarConstants.FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE;
import static com.rafaelwaldo.taxis.car.utils.MockHelper.getMockTaxi;
import static com.rafaelwaldo.taxis.car.utils.MockHelper.getMockTrip;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxiCentralClientImplTest {

    private static final String TAXI_CENTRAL_HOST = "http://localhost:8080";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TaxiCentralClientImpl taxiCentralClient;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        restTemplate = Mockito.mock(RestTemplate.class);
        taxiCentralClient = new TaxiCentralClientImpl(restTemplate);
        Field field = TaxiCentralClientImpl.class.getDeclaredField("taxiCentralHost");
        field.setAccessible(true);
        field.set(taxiCentralClient, TAXI_CENTRAL_HOST);
    }

    @Test
    void publishTaxis_success() {
        Taxi taxi = getMockTaxi().build();
        ResponseEntity<Taxi> responseEntity = new ResponseEntity<>(taxi, HttpStatus.OK);

        when(restTemplate.postForEntity("http://localhost:8080/central/taxi", taxi, Taxi.class)).thenReturn(responseEntity);
        taxiCentralClient.publishTaxis(taxi);

        verify(restTemplate).postForEntity("http://localhost:8080/central/taxi", taxi, Taxi.class);
    }

    @Test
    void publishTaxis_failure() {
        Taxi taxi = getMockTaxi().build();
        ResponseEntity<Taxi> responseEntity = new ResponseEntity<>(taxi, HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.postForEntity("http://localhost:8080/central/taxi", taxi, Taxi.class)).thenReturn(responseEntity);

        CentralException exception = assertThrows(CentralException.class, () -> taxiCentralClient.publishTaxis(taxi));
        assertEquals(FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE + "500 INTERNAL_SERVER_ERROR", exception.getMessage());
    }

    @Test
    void updateTripTaxi_success() {
        Taxi taxi = getMockTaxi().build();
        Trip trip = getMockTrip().build();
        ResponseEntity<Trip> responseEntity = new ResponseEntity<>(trip, HttpStatus.OK);

        RequestEntity<Taxi> requestEntity = RequestEntity
                .put("http://localhost:8080/central/trip/" + trip.uuid() + "/taxi")
                .body(taxi);
        when(restTemplate.exchange(requestEntity, Trip.class)).thenReturn(responseEntity);

        taxiCentralClient.updateTripTaxi(trip.uuid(), taxi);

        verify(restTemplate).exchange(requestEntity, Trip.class);
    }

    @Test
    void updateTripTaxi_failure() {
        Taxi taxi = getMockTaxi().build();
        Trip trip = getMockTrip().taxi(taxi).build();
        UUID tripUuid = trip.uuid();
        ResponseEntity<Trip> responseEntity = new ResponseEntity<>(trip, HttpStatus.INTERNAL_SERVER_ERROR);

        RequestEntity<Taxi> requestEntity = RequestEntity
                .put("http://localhost:8080/central/trip/" + trip.uuid() + "/taxi")
                .body(taxi);
        when(restTemplate.exchange(requestEntity, Trip.class)).thenReturn(responseEntity);

        CentralException exception = assertThrows(CentralException.class, () -> taxiCentralClient.updateTripTaxi(tripUuid, taxi));
        assertEquals(FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE + "500 INTERNAL_SERVER_ERROR", exception.getMessage());
    }

    @Test
    void updateTripStatus_success() {
        Trip trip = getMockTrip().build();
        ResponseEntity<Trip> responseEntity = new ResponseEntity<>(trip, HttpStatus.OK);

        RequestEntity<TripStatus> requestEntity = RequestEntity
                .put("http://localhost:8080/central/trip/" + trip.uuid() + "/tripStatus")
                .body(TripStatus.ASSIGNED);
        when(restTemplate.exchange(requestEntity, Trip.class)).thenReturn(responseEntity);

        taxiCentralClient.updateTripStatus(trip.uuid(), TripStatus.ASSIGNED);

        verify(restTemplate).exchange(requestEntity, Trip.class);
    }

    @Test
    void updateTripStatus_failure() {
        Trip trip = getMockTrip().build();
        UUID tripUuid = trip.uuid();
        ResponseEntity<Trip> responseEntity = new ResponseEntity<>(trip, HttpStatus.INTERNAL_SERVER_ERROR);

        RequestEntity<TripStatus> requestEntity = RequestEntity
                .put("http://localhost:8080/central/trip/" + trip.uuid() + "/tripStatus")
                .body(TripStatus.ASSIGNED);
        when(restTemplate.exchange(requestEntity, Trip.class)).thenReturn(responseEntity);

        CentralException exception = assertThrows(CentralException.class, () -> taxiCentralClient.updateTripStatus(tripUuid, TripStatus.ASSIGNED));
        assertEquals(FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE + "500 INTERNAL_SERVER_ERROR", exception.getMessage());
    }


}