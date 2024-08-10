package com.rafaelwaldo.taxis.hub.domain.dto;

import com.rafaelwaldo.taxis.hub.domain.Location;
import com.rafaelwaldo.taxis.hub.domain.TaxiStatus;

public record TaxiDTO(String plate, TaxiStatus taxiStatus, Location location) {
}
