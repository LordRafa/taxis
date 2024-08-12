package com.rafaelwaldo.taxis.car.domain.pojo;

import com.rafaelwaldo.taxis.car.domain.Location;
import com.rafaelwaldo.taxis.car.domain.TaxiStatus;
import com.rafaelwaldo.taxis.car.domain.Trip;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TaxiPojo {
    UUID uuid;
    String plate;
    TaxiStatus taxiStatus;
    Location location;
    Trip currentTrip;
}
