package com.rafaelwaldo.taxis.central.domain.service;

import com.rafaelwaldo.taxis.central.domain.Taxi;

import java.util.List;

public interface TaxiService {
    List<Taxi> listTaxis();

    Taxi publishTaxi(Taxi taxi);
}
