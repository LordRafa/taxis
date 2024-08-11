package com.rafaelwaldo.taxis.central.repository.entity;

import com.rafaelwaldo.taxis.central.domain.TripStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String latitude;
    private String longitude;

    @With
    private TripStatus tripStatus;

    @With
    @ManyToOne
    @JoinColumn(name = "taxi_uuid")
    private TaxiEntity taxi;

}
