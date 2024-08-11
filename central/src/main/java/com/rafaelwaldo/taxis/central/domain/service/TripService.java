package com.rafaelwaldo.taxis.central.domain.service;

import com.rafaelwaldo.taxis.central.domain.Taxi;
import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.domain.TripStatus;

import java.util.UUID;

public interface TripService {
    Trip publishTrip(Trip trip);

    Taxi getAssignedTripTaxi(UUID uuid);

    Trip updateTripTaxi(UUID tripUuid, Taxi taxi);

    Trip updateTripStatus(UUID tripUuid, TripStatus tripStatus);
}
