package com.rafaelwaldo.taxis.central.repository;

import com.rafaelwaldo.taxis.central.domain.TaxiStatus;
import com.rafaelwaldo.taxis.central.repository.entity.TaxiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaxiRepository extends JpaRepository<TaxiEntity, UUID> {
    long countByTaxiStatus(TaxiStatus taxiStatus);
}
