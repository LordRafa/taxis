package com.rafaelwaldo.taxis.car.domain.service;

import com.rafaelwaldo.taxis.car.domain.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DummyServiceLocationImplTest {

    private final DummyServiceLocationImpl dummyServiceLocation = new DummyServiceLocationImpl();

    @Test
    void testGetCurrentLocation() {
        Location expectedLocation = new Location("foo", "bar");
        Location actualLocation = dummyServiceLocation.getCurrentLocation();

        assertEquals(expectedLocation, actualLocation);
    }
}