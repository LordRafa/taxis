package com.rafaelwaldo.taxis.hub.domain.service;

import com.rafaelwaldo.taxis.hub.domain.Taxi;
import com.rafaelwaldo.taxis.hub.domain.Trip;

import java.util.List;

public interface TaxiHubService {

    List<Taxi> listTaxis();

    Taxi requestTaxi(Trip trip);

}
