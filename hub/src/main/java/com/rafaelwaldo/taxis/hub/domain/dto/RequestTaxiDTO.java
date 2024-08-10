package com.rafaelwaldo.taxis.hub.domain.dto;

import com.rafaelwaldo.taxis.hub.domain.Location;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RequestTaxiDTO(@NotNull Location location) {
}
