package com.rafaelwaldo.taxis.central.domain.service;

import com.rafaelwaldo.taxis.central.domain.Trip;

public interface CommandService {
    void sendPublishTripCommand(Trip tripUuid);
}
