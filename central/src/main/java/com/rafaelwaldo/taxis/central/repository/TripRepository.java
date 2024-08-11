package com.rafaelwaldo.taxis.central.repository;

import com.rafaelwaldo.taxis.central.repository.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<TripEntity, UUID> {
}