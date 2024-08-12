package com.rafaelwaldo.taxis.central.domain;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Taxi(UUID uuid, String plate, TaxiStatus taxiStatus, Location location, UUID currentTripUuid) {
}
