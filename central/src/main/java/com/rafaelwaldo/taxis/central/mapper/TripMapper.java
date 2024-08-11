package com.rafaelwaldo.taxis.central.mapper;

import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.repository.entity.TripEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TaxiMapper.class})
public interface TripMapper {

    @Mapping(target = "latitude", source = "location.latitude")
    @Mapping(target = "longitude", source = "location.longitude")
    TripEntity toEntity(Trip trip);

    @Mapping(target = "location.latitude", source = "latitude")
    @Mapping(target = "location.longitude", source = "longitude")

    Trip toDomain(TripEntity tripEntity);
}
