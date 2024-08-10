package com.rafaelwaldo.taxis.hub.mapper;

import com.rafaelwaldo.taxis.hub.domain.Trip;
import com.rafaelwaldo.taxis.hub.domain.dto.RequestTaxiDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "tripStatus", constant = "PENDING")
    Trip toTrip(RequestTaxiDTO requestTaxiDTO);

}
