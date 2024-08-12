package com.rafaelwaldo.taxis.hub.service;

import com.rafaelwaldo.taxis.hub.domain.Taxi;
import com.rafaelwaldo.taxis.hub.domain.Trip;
import com.rafaelwaldo.taxis.hub.domain.exception.TripNotFoundException;
import com.rafaelwaldo.taxis.hub.domain.service.TaxiHubServiceImpl;
import com.rafaelwaldo.taxis.hub.infrastructure.TaxiCentralClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.rafaelwaldo.taxis.hub.utils.MockHelper.getMockTaxi;
import static com.rafaelwaldo.taxis.hub.utils.MockHelper.getMockTrip;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxiHubServiceImplTest {

    @Mock
    private TaxiCentralClient taxiCentralClient;

    @InjectMocks
    private TaxiHubServiceImpl taxiHubService;


    @Test
    void testListTaxis() {
        Taxi taxi = getMockTaxi().build();

        when(taxiCentralClient.listTaxis()).thenReturn(List.of(taxi));

        List<Taxi> taxis = taxiHubService.listTaxis();

        assertEquals(1, taxis.size());
        assertEquals(taxi, taxis.get(0));
    }

    @Test
    void testRequestTaxi() {

        Trip trip = getMockTrip().uuid(null).build();
        Taxi taxi = getMockTaxi().build();
        Trip tripWithUUid = getMockTrip().build();

        when(taxiCentralClient.publishTrip(trip)).thenReturn(tripWithUUid);
        when(taxiCentralClient.getAssignedTripTaxi(tripWithUUid.uuid())).thenReturn(taxi);

        Taxi result = taxiHubService.requestTaxi(trip);

        assertEquals(taxi, result);
    }

    @Test
    void testRequestTaxiError() {

        Trip trip = getMockTrip().uuid(null).build();
        Trip tripWithUUid = getMockTrip().build();

        when(taxiCentralClient.publishTrip(trip)).thenReturn(tripWithUUid);
        when(taxiCentralClient.getAssignedTripTaxi(tripWithUUid.uuid())).thenThrow(new TripNotFoundException("Trip not found"));

        assertThrows(TripNotFoundException.class, () -> taxiHubService.requestTaxi(trip));
        Mockito.verify(taxiCentralClient).cancelTrip(tripWithUUid.uuid());

    }
}