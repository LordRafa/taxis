package com.rafaelwaldo.taxis.car.domain;

import lombok.Builder;

@Builder
public record TaxiCommand(
        TaxiCommandName taxiCommandName,
        String args
) {
}
