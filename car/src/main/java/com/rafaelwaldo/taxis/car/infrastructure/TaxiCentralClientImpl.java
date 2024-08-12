package com.rafaelwaldo.taxis.car.infrastructure;

import com.rafaelwaldo.taxis.car.domain.Taxi;
import com.rafaelwaldo.taxis.car.domain.Trip;
import com.rafaelwaldo.taxis.car.domain.TripStatus;
import com.rafaelwaldo.taxis.car.domain.exception.CentralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.rafaelwaldo.taxis.car.util.CarConstants.FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaxiCentralClientImpl implements TaxiCentralClient {

    private final RestTemplate restTemplate;

    @Value("${taxi.central.host}")
    private String taxiCentralHost;

    @Override
    public Taxi publishTaxis(Taxi taxi) {
        ResponseEntity<Taxi> response = restTemplate.postForEntity(taxiCentralHost + "/central/taxi", taxi, Taxi.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new CentralException(FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE + response.getStatusCode());
        }
    }

    @Override
    public void updateTripTaxi(UUID tripUuid, Taxi taxi) {

        RequestEntity<Taxi> requestEntity = RequestEntity
                .put(taxiCentralHost + "/central/trip/" + tripUuid + "/taxi")
                .body(taxi);
        ResponseEntity<Trip> response = restTemplate.exchange(requestEntity, Trip.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CentralException(FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE + response.getStatusCode());
        }
    }

    @Override
    public void updateTripStatus(UUID tripUuid, TripStatus tripStatus) {

        RequestEntity<TripStatus> requestEntity = RequestEntity
                .put(taxiCentralHost + "/central/trip/" + tripUuid + "/tripStatus")
                .body(tripStatus);
        ResponseEntity<Trip> response = restTemplate.exchange(requestEntity, Trip.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CentralException(FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE + response.getStatusCode());
        }
    }
}
