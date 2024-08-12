package com.rafaelwaldo.taxis.central.domain.service;

import com.rafaelwaldo.taxis.central.domain.Stats;
import com.rafaelwaldo.taxis.central.domain.TaxiStatus;
import com.rafaelwaldo.taxis.central.domain.TripStatus;
import com.rafaelwaldo.taxis.central.repository.TaxiRepository;
import com.rafaelwaldo.taxis.central.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @Mock
    private TaxiRepository taxiRepository;

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private StatsServiceImpl statsService;

    @Test
    void testGetStats() {
        // Mocking the repository methods
        when(taxiRepository.count()).thenReturn(100L);
        when(taxiRepository.countByTaxiStatus(TaxiStatus.AVAILABLE)).thenReturn(60L);
        when(taxiRepository.countByTaxiStatus(TaxiStatus.BOOKED)).thenReturn(40L);

        when(tripRepository.count()).thenReturn(200L);
        when(tripRepository.countByTripStatus(TripStatus.PENDING)).thenReturn(50L);
        when(tripRepository.countByTripStatus(TripStatus.ASSIGNED)).thenReturn(30L);
        when(tripRepository.countByTripStatus(TripStatus.COMPLETED)).thenReturn(100L);
        when(tripRepository.countByTripStatus(TripStatus.CANCELED)).thenReturn(20L);

        // Calling the method under test
        Stats stats = statsService.getStats();

        // Verifying the results
        assertThat(stats.taxiStats().totalTaxis()).isEqualTo(100L);
        assertThat(stats.taxiStats().totalAvailableTaxis()).isEqualTo(60L);
        assertThat(stats.taxiStats().totalBookedTaxis()).isEqualTo(40L);

        assertThat(stats.tripStats().totalTrips()).isEqualTo(200L);
        assertThat(stats.tripStats().totalPendingTrips()).isEqualTo(50L);
        assertThat(stats.tripStats().totalAssignedTrips()).isEqualTo(30L);
        assertThat(stats.tripStats().totalCompletedTrips()).isEqualTo(100L);
        assertThat(stats.tripStats().totalCanceledTrips()).isEqualTo(20L);
    }
}