package com.rafaelwaldo.taxis.hub.mapper;

import com.rafaelwaldo.taxis.hub.domain.Taxi;
import com.rafaelwaldo.taxis.hub.domain.dto.TaxiDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaxiMapper {
    TaxiDTO toTaxiDTO(Taxi taxi);
}
