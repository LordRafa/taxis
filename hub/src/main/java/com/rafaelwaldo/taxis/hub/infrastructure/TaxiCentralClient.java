package com.rafaelwaldo.taxis.hub.infrastructure;

import com.rafaelwaldo.taxis.hub.domain.Taxi;
import com.rafaelwaldo.taxis.hub.domain.Trip;

import java.util.List;
import java.util.UUID;

public interface TaxiCentralClient {
    List<Taxi> listTaxis();

    Trip publishTrip(Trip trip);

    Taxi getAssignedTripTaxi(UUID tripUuid);
}
