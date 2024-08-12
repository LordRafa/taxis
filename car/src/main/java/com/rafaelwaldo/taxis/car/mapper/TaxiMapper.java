package com.rafaelwaldo.taxis.car.mapper;

import com.rafaelwaldo.taxis.car.domain.Taxi;
import com.rafaelwaldo.taxis.car.domain.pojo.TaxiPojo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaxiMapper {
    @Mapping(target = "currentTripUuid", source = "currentTrip.uuid")
    Taxi toDomain(TaxiPojo taxiEntity);
}
