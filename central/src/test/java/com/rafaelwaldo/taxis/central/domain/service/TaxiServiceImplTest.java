package com.rafaelwaldo.taxis.central.domain.service;

import com.rafaelwaldo.taxis.central.domain.Taxi;
import com.rafaelwaldo.taxis.central.mapper.TaxiMapper;
import com.rafaelwaldo.taxis.central.repository.TaxiRepository;
import com.rafaelwaldo.taxis.central.repository.entity.TaxiEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.rafaelwaldo.taxis.central.utils.MockHelper.getMockTaxi;
import static com.rafaelwaldo.taxis.central.utils.MockHelper.getMockTaxiEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxiServiceImplTest {

    @Mock
    private TaxiRepository taxiRepository;

    @Mock
    private TaxiMapper taxiMapper;

    @InjectMocks
    private TaxiServiceImpl taxiService;

    @Test
    void listTaxis() {

        TaxiEntity taxiEntity = getMockTaxiEntity().build();
        Taxi taxi = getMockTaxi().build();

        when(taxiRepository.findAll()).thenReturn(List.of(taxiEntity));
        when(taxiMapper.toDomain(taxiEntity)).thenReturn(taxi);

        List<Taxi> result = taxiService.listTaxis();

        assertEquals(1, result.size());
        assertEquals(taxi, result.get(0));

        verify(taxiRepository, times(1)).findAll();
        verify(taxiMapper, times(1)).toDomain(taxiEntity);
    }

    @Test
    void publishTaxi() {
        Taxi taxi = getMockTaxi().build();
        TaxiEntity taxiEntity = getMockTaxiEntity().build();

        when(taxiMapper.toEntity(taxi)).thenReturn(taxiEntity);
        when(taxiRepository.save(taxiEntity)).thenReturn(taxiEntity);
        when(taxiMapper.toDomain(taxiEntity)).thenReturn(taxi);

        Taxi result = taxiService.publishTaxi(taxi);

        assertEquals(taxi, result);
        verify(taxiMapper, times(1)).toEntity(taxi);
        verify(taxiRepository, times(1)).save(taxiEntity);
        verify(taxiMapper, times(1)).toDomain(taxiEntity);
    }
}