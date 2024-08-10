package com.rafaelwaldo.taxis.hub.infrastructure;

import com.rafaelwaldo.taxis.hub.domain.Taxi;
import com.rafaelwaldo.taxis.hub.domain.Trip;
import com.rafaelwaldo.taxis.hub.domain.exception.CentralException;
import com.rafaelwaldo.taxis.hub.domain.exception.TripNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.rafaelwaldo.taxis.hub.util.HubConstants.FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaxiCentralClientImpl implements TaxiCentralClient {

    private final RestTemplate restTemplate;
    @Value("${taxi.central.host}")
    private String taxiCentralHost;

    @Override
    public List<Taxi> listTaxis() {
        ResponseEntity<Taxi[]> response = restTemplate.getForEntity(taxiCentralHost + "/taxi", Taxi[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } else {
            throw new CentralException(FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE + response.getStatusCode());
        }
    }

    @Override
    public Trip publishTrip(Trip trip) {
        ResponseEntity<Trip> response = restTemplate.postForEntity(taxiCentralHost + "/trip", trip, Trip.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new CentralException(FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE + response.getStatusCode());
        }
    }

    @Override
    @Retryable(retryFor = TripNotFoundException.class, backoff = @Backoff(delay = 1000))
    public Taxi getAssignedTripTaxi(UUID tripUuid) {
        ResponseEntity<Taxi> response = restTemplate.getForEntity(taxiCentralHost + "/trip/" + tripUuid + "/taxi", Taxi.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else if (response.getStatusCode().is4xxClientError()) {
            throw new TripNotFoundException("Trip not found");
        } else {
            throw new CentralException(FAILED_TO_COMMUNICATE_WITH_CENTRAL_HTTP_ERROR_CODE + response.getStatusCode());
        }
    }
}
