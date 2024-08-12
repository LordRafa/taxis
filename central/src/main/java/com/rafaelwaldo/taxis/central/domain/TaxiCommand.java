package com.rafaelwaldo.taxis.central.domain;

import lombok.Builder;

@Builder
public record TaxiCommand(
        TaxiCommandName taxiCommandName,
        String args
) {
}
