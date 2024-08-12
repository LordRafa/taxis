package com.rafaelwaldo.taxis.central.domain;

import lombok.Builder;

@Builder
public record Stats(TaxiStats taxiStats, TripStats tripStats) {
}
