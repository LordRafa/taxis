package com.rafaelwaldo.taxis.central.domain;

import lombok.Builder;

@Builder
public record TaxiStats(
        long totalTaxis,
        long totalAvailableTaxis,
        long totalBookedTaxis
) {
}
