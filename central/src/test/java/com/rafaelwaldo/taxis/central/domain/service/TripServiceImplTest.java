package com.rafaelwaldo.taxis.central.domain.service;

import com.rafaelwaldo.taxis.central.domain.Taxi;
import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.domain.TripStatus;
import com.rafaelwaldo.taxis.central.domain.exception.TripNotFoundException;
import com.rafaelwaldo.taxis.central.mapper.TaxiMapper;
import com.rafaelwaldo.taxis.central.mapper.TripMapper;
import com.rafaelwaldo.taxis.central.repository.TripRepository;
import com.rafaelwaldo.taxis.central.repository.entity.TaxiEntity;
import com.rafaelwaldo.taxis.central.repository.entity.TripEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.rafaelwaldo.taxis.central.domain.TripStatus.PENDING;
import static com.rafaelwaldo.taxis.central.utils.MockHelper.getMockTaxi;
import static com.rafaelwaldo.taxis.central.utils.MockHelper.getMockTaxiEntity;
import static com.rafaelwaldo.taxis.central.utils.MockHelper.getMockTrip;
import static com.rafaelwaldo.taxis.central.utils.MockHelper.getMockTripEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripServiceImplTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private CommandService commandService;

    @Mock
    private TripMapper tripMapper;

    @Mock
    private TaxiMapper taxiMapper;

    @InjectMocks
    private TripServiceImpl tripService;

    @Test
    void publishTrip() {
        Trip trip = getMockTrip().uuid(null).build();
        TripEntity tripEntity = getMockTripEntity().uuid(null).build();
        TripEntity tripEntitySaved = getMockTripEntity().build();
        Trip tripSaved = getMockTrip().build();

        when(tripMapper.toEntity(trip)).thenReturn(tripEntity);
        when(tripRepository.save(tripEntity)).thenReturn(tripEntitySaved);
        when(tripMapper.toDomain(tripEntitySaved)).thenReturn(tripSaved);

        Trip result = tripService.publishTrip(trip);

        assertEquals(tripSaved, result);
        verify(commandService, times(1)).sendPublishTripCommand(tripSaved);
    }

    @Test
    void getAssignedTripTaxi() {
        Trip trip = getMockTrip().build();
        Taxi taxi = getMockTaxi().build();
        TaxiEntity taxiEntity = getMockTaxiEntity().uuid(taxi.uuid()).build();
        TripEntity tripEntity = getMockTripEntity().uuid(trip.uuid()).taxi(taxiEntity).build();

        when(tripRepository.findById(trip.uuid())).thenReturn(Optional.of(tripEntity));
        when(taxiMapper.toDomain(taxiEntity)).thenReturn(taxi);

        Taxi result = tripService.getAssignedTripTaxi(trip.uuid());

        assertEquals(taxi, result);
    }

    @Test
    void getAssignedTripTaxi_TripNotFound() {
        UUID tripId = UUID.randomUUID();

        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(TripNotFoundException.class, () -> tripService.getAssignedTripTaxi(tripId));
    }

    @Test
    void updateTripTaxi() {
        UUID tripUuid = UUID.randomUUID();
        Taxi taxi = getMockTaxi().build();
        TaxiEntity taxiEntity = getMockTaxiEntity().build();
        TripEntity tripEntity = getMockTripEntity().tripStatus(PENDING).build();
        TripEntity updatedTripEntity = tripEntity.withTripStatus(TripStatus.ASSIGNED).withTaxi(taxiEntity);
        Trip updatedTrip = getMockTrip().tripStatus(TripStatus.ASSIGNED).build();

        when(tripRepository.findById(tripUuid)).thenReturn(Optional.of(tripEntity));
        when(taxiMapper.toEntity(taxi)).thenReturn(taxiEntity);
        when(tripRepository.save(updatedTripEntity)).thenReturn(updatedTripEntity);
        when(tripMapper.toDomain(updatedTripEntity)).thenReturn(updatedTrip);

        Trip result = tripService.updateTripTaxi(tripUuid, taxi);

        assertEquals(updatedTrip, result);
        verify(tripRepository, times(1)).findById(tripUuid);
        verify(taxiMapper, times(1)).toEntity(taxi);
        verify(tripRepository, times(1)).save(updatedTripEntity);
        verify(tripMapper, times(1)).toDomain(updatedTripEntity);
    }

    @Test
    void updateTripTaxi_TripNotFound() {
        UUID tripUuid = UUID.randomUUID();
        Taxi taxi = getMockTaxi().build();

        when(tripRepository.findById(tripUuid)).thenReturn(Optional.empty());

        assertThrows(TripNotFoundException.class, () -> tripService.updateTripTaxi(tripUuid, taxi));
    }

    @Test
    void updateTripStatus() {
        UUID tripUuid = UUID.randomUUID();
        TripStatus tripStatus = TripStatus.COMPLETED;
        TripEntity tripEntity = getMockTripEntity().build();
        TripEntity updatedTripEntity = tripEntity.withTripStatus(tripStatus);
        Trip updatedTrip = getMockTrip().tripStatus(tripStatus).build();

        when(tripRepository.findById(tripUuid)).thenReturn(Optional.of(tripEntity));
        when(tripRepository.save(updatedTripEntity)).thenReturn(updatedTripEntity);
        when(tripMapper.toDomain(updatedTripEntity)).thenReturn(updatedTrip);

        Trip result = tripService.updateTripStatus(tripUuid, tripStatus);

        assertEquals(updatedTrip, result);
        verify(tripRepository, times(1)).findById(tripUuid);
        verify(tripRepository, times(1)).save(updatedTripEntity);
        verify(tripMapper, times(1)).toDomain(updatedTripEntity);
    }

    @Test
    void updateTripStatus_TripNotFound() {
        UUID tripUuid = UUID.randomUUID();
        TripStatus tripStatus = TripStatus.COMPLETED;

        when(tripRepository.findById(tripUuid)).thenReturn(Optional.empty());

        assertThrows(TripNotFoundException.class, () -> tripService.updateTripStatus(tripUuid, tripStatus));
    }

}