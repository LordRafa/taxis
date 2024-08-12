package com.rafaelwaldo.taxis.car.utils;

import com.rafaelwaldo.taxis.car.domain.Location;
import com.rafaelwaldo.taxis.car.domain.Taxi;
import com.rafaelwaldo.taxis.car.domain.Taxi.TaxiBuilder;
import com.rafaelwaldo.taxis.car.domain.TaxiStatus;
import com.rafaelwaldo.taxis.car.domain.Trip;
import com.rafaelwaldo.taxis.car.domain.Trip.TripBuilder;
import com.rafaelwaldo.taxis.car.domain.TripStatus;

import java.util.UUID;


public class MockHelper {

    private MockHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static TaxiBuilder getMockTaxi() {
        return Taxi.builder()
                .uuid(UUID.randomUUID())
                .plate("ABC1234")
                .taxiStatus(TaxiStatus.AVAILABLE)
                .location(new Location("foo", "bar"));
    }

    public static TripBuilder getMockTrip() {
        return Trip.builder()
                .uuid(UUID.randomUUID())
                .location(new Location("foo", "bar"))
                .tripStatus(TripStatus.PENDING);
    }
}
