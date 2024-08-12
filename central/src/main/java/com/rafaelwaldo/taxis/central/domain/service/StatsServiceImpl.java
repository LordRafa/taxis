package com.rafaelwaldo.taxis.central.domain.service;

import com.rafaelwaldo.taxis.central.domain.Stats;
import com.rafaelwaldo.taxis.central.domain.TaxiStats;
import com.rafaelwaldo.taxis.central.domain.TaxiStatus;
import com.rafaelwaldo.taxis.central.domain.TripStats;
import com.rafaelwaldo.taxis.central.domain.TripStatus;
import com.rafaelwaldo.taxis.central.repository.TaxiRepository;
import com.rafaelwaldo.taxis.central.repository.TripRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final TaxiRepository taxiRepository;
    private final TripRepository tripRepository;

    @Override
    public Stats getStats() {

        return Stats.builder()
                .taxiStats(TaxiStats.builder()
                        .totalTaxis(taxiRepository.count())
                        .totalAvailableTaxis(taxiRepository.countByTaxiStatus(TaxiStatus.AVAILABLE))
                        .totalBookedTaxis(taxiRepository.countByTaxiStatus(TaxiStatus.BOOKED))
                        .build())
                .tripStats(TripStats.builder()
                        .totalTrips(tripRepository.count())
                        .totalPendingTrips(tripRepository.countByTripStatus(TripStatus.PENDING))
                        .totalAssignedTrips(tripRepository.countByTripStatus(TripStatus.ASSIGNED))
                        .totalCompletedTrips(tripRepository.countByTripStatus(TripStatus.COMPLETED))
                        .totalCanceledTrips(tripRepository.countByTripStatus(TripStatus.CANCELED))
                        .build())
                .build();
    }
}
