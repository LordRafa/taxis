package com.rafaelwaldo.taxis.hub.domain.service;

import com.rafaelwaldo.taxis.hub.domain.Taxi;
import com.rafaelwaldo.taxis.hub.domain.Trip;
import com.rafaelwaldo.taxis.hub.infrastructure.TaxiCentralClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxiHubServiceImpl implements TaxiHubService {

    private final TaxiCentralClient taxiCentralClient;

    @Override
    public List<Taxi> listTaxis() {
        return taxiCentralClient.listTaxis();
    }

    @Override
    public Taxi requestTaxi(Trip trip) {
        trip = taxiCentralClient.publishTrip(trip);
        return taxiCentralClient.getAssignedTripTaxi(trip.uuid());
    }
}
