package com.rafaelwaldo.taxis.hub.domain;

import jakarta.validation.constraints.NotBlank;

public record Location(@NotBlank String latitude, @NotBlank String longitude) {

}
