package com.rafaelwaldo.taxis.central.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record TaxiCommand(
        TaxiCommandName taxiCommandName,
        List<Object> args
) {
}
