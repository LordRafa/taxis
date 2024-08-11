package com.rafaelwaldo.taxis.central.domain;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Trip(
        UUID uuid,
        Location location,
        TripStatus tripStatus,
        Taxi taxi
) {

}
