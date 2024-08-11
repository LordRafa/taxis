package com.rafaelwaldo.taxis.central.domain;

import jakarta.validation.constraints.NotBlank;

public record Location(@NotBlank String latitude, @NotBlank String longitude) {

}
