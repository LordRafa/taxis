package com.rafaelwaldo.taxis.hub.domain.exception;

import lombok.Getter;

@Getter
public class TripNotFoundException extends RuntimeException {

    public TripNotFoundException(String message) {
        super(message);
    }

}
