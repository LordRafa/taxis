package com.rafaelwaldo.taxis.central.repository.entity;

import com.rafaelwaldo.taxis.central.domain.TaxiStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private String plate;
    private TaxiStatus taxiStatus;
    private String latitude;
    private String longitude;
    private UUID currentTripUuid;
}