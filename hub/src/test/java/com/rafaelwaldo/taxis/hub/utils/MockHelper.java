package com.rafaelwaldo.taxis.hub.utils;

import com.rafaelwaldo.taxis.hub.domain.Location;
import com.rafaelwaldo.taxis.hub.domain.Taxi;
import com.rafaelwaldo.taxis.hub.domain.Taxi.TaxiBuilder;
import com.rafaelwaldo.taxis.hub.domain.TaxiStatus;
import com.rafaelwaldo.taxis.hub.domain.Trip;
import com.rafaelwaldo.taxis.hub.domain.Trip.TripBuilder;
import com.rafaelwaldo.taxis.hub.domain.TripStatus;
import com.rafaelwaldo.taxis.hub.domain.dto.RequestTaxiDTO;
import com.rafaelwaldo.taxis.hub.domain.dto.RequestTaxiDTO.RequestTaxiDTOBuilder;

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

    public static RequestTaxiDTOBuilder getMockRequestTaxiDTO() {
        return RequestTaxiDTO.builder().location(new Location("foo", "bar"));
    }
}
