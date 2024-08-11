package com.rafaelwaldo.taxis.central.domain.service;

import com.rafaelwaldo.taxis.central.domain.Taxi;
import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.domain.TripStatus;
import com.rafaelwaldo.taxis.central.domain.exception.TripNotFoundException;
import com.rafaelwaldo.taxis.central.mapper.TaxiMapper;
import com.rafaelwaldo.taxis.central.mapper.TripMapper;
import com.rafaelwaldo.taxis.central.repository.TripRepository;
import com.rafaelwaldo.taxis.central.repository.entity.TripEntity;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TripServiceImpl implements TripService {

    public static final String TRIP_NOT_FOUND = "Trip not found";
    private final TripRepository tripRepository;
    private final CommandService commandService;

    private final TripMapper tripMapper;
    private final TaxiMapper taxiMapper;

    @Override
    public Trip publishTrip(Trip trip) {
        Trip tripSaved = tripMapper.toDomain(tripRepository.save(tripMapper.toEntity(trip)));
        commandService.sendPublishTripCommand(tripSaved);
        return tripSaved;
    }

    @Override
    public Taxi getAssignedTripTaxi(UUID uuid) {
        return taxiMapper.toDomain(
                tripRepository.findById(uuid)
                        .map(TripEntity::getTaxi)
                        .orElseThrow(() -> new TripNotFoundException(TRIP_NOT_FOUND)));
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Trip updateTripTaxi(UUID tripUuid, Taxi taxi) {
        return tripRepository.findById(tripUuid)
                .filter(tripEntity -> tripEntity.getTripStatus().equals(TripStatus.PENDING))
                .map(te -> te.withTripStatus(TripStatus.ASSIGNED))
                .map(te -> te.withTaxi(taxiMapper.toEntity(taxi)))
                .map(te -> tripMapper.toDomain(tripRepository.save(te)))
                .orElseThrow(() -> new TripNotFoundException(TRIP_NOT_FOUND));
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Trip updateTripStatus(UUID tripUuid, TripStatus tripStatus) {
        return tripRepository.findById(tripUuid)
                .map(te -> te.withTripStatus(tripStatus))
                .map(te -> tripMapper.toDomain(tripRepository.save(te)))
                .orElseThrow(() -> new TripNotFoundException(TRIP_NOT_FOUND));
    }
}
