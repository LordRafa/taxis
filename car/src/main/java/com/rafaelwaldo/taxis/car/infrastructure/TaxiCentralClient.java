package com.rafaelwaldo.taxis.car.infrastructure;

import com.rafaelwaldo.taxis.car.domain.Taxi;
import com.rafaelwaldo.taxis.car.domain.TripStatus;

import java.util.UUID;

public interface TaxiCentralClient {
    Taxi publishTaxis(Taxi taxi);

    void updateTripTaxi(UUID tripUuid, Taxi taxi);

    void updateTripStatus(UUID tripUuid, TripStatus tripStatus);
}
