package com.rafaelwaldo.taxis.car.domain.service;

import com.rafaelwaldo.taxis.car.domain.Location;
import org.springframework.stereotype.Service;

@Service
public class DummyServiceLocationImpl implements ServiceLocation {
    @Override
    public Location getCurrentLocation() {
        return new Location("foo", "bar");
    }
}
