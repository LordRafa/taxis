package com.rafaelwaldo.taxis.central.repository;

import com.rafaelwaldo.taxis.central.domain.TripStatus;
import com.rafaelwaldo.taxis.central.repository.entity.TripEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<TripEntity, UUID> {

    @NonNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TripEntity> findById(@NonNull UUID id);

    @NonNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    TripEntity save(@NonNull TripEntity tripEntity);

    long countByTripStatus(@NonNull TripStatus tripStatus);
}