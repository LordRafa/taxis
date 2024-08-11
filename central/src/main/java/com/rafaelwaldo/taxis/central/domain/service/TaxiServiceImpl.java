package com.rafaelwaldo.taxis.central.domain.service;

import com.rafaelwaldo.taxis.central.domain.Taxi;
import com.rafaelwaldo.taxis.central.mapper.TaxiMapper;
import com.rafaelwaldo.taxis.central.repository.TaxiRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaxiServiceImpl implements TaxiService {

    private final TaxiRepository taxiRepository;
    private final TaxiMapper taxiMapper;

    @Override
    public List<Taxi> listTaxis() {
        return taxiRepository.findAll().stream().map(taxiMapper::toDomain).toList();
    }

    @Override
    public Taxi publishTaxi(Taxi taxi) {
        return taxiMapper.toDomain(taxiRepository.save(taxiMapper.toEntity(taxi)));
    }
}
