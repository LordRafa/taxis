package com.rafaelwaldo.taxis.central.utils;

import com.rafaelwaldo.taxis.central.domain.Location;
import com.rafaelwaldo.taxis.central.domain.Taxi;
import com.rafaelwaldo.taxis.central.domain.Taxi.TaxiBuilder;
import com.rafaelwaldo.taxis.central.domain.TaxiStatus;
import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.domain.Trip.TripBuilder;
import com.rafaelwaldo.taxis.central.domain.TripStatus;
import com.rafaelwaldo.taxis.central.repository.entity.TaxiEntity;
import com.rafaelwaldo.taxis.central.repository.entity.TaxiEntity.TaxiEntityBuilder;
import com.rafaelwaldo.taxis.central.repository.entity.TripEntity;
import com.rafaelwaldo.taxis.central.repository.entity.TripEntity.TripEntityBuilder;

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
                .location(new Location("0.0", "0.0"));
    }

    public static TripBuilder getMockTrip() {
        return Trip.builder()
                .uuid(UUID.randomUUID())
                .location(new Location("foo", "bar"))
                .tripStatus(TripStatus.PENDING);
    }

    public static TaxiEntityBuilder getMockTaxiEntity() {
        return TaxiEntity.builder()
                .uuid(UUID.randomUUID())
                .plate("ABC1234")
                .taxiStatus(TaxiStatus.AVAILABLE)
                .latitude("0.0")
                .longitude("0.0");
    }

    public static TripEntityBuilder getMockTripEntity() {
        return TripEntity.builder()
                .uuid(UUID.randomUUID())
                .latitude("foo")
                .longitude("bar")
                .tripStatus(TripStatus.PENDING);
    }

}
