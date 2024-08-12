package com.rafaelwaldo.taxis.car.domain;

import lombok.Builder;
import lombok.With;

import java.util.UUID;

@Builder
@With
public record Taxi(UUID uuid, String plate, TaxiStatus taxiStatus, Location location, UUID currentTripUuid) {
}
