package com.rafaelwaldo.taxis.central.domain;

import lombok.Builder;

@Builder
public record TripStats(
        long totalTrips,
        long totalPendingTrips,
        long totalAssignedTrips,
        long totalCompletedTrips,
        long totalCanceledTrips
) {
}
