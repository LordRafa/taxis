package com.rafaelwaldo.taxis.central.controller;

import com.rafaelwaldo.taxis.central.domain.Taxi;
import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.domain.TripStatus;
import com.rafaelwaldo.taxis.central.domain.exception.TripNotFoundException;
import com.rafaelwaldo.taxis.central.domain.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/central/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping("{tripUuid}/taxi")
    public Taxi getAssignedTripTaxi(@PathVariable UUID tripUuid) {
        return tripService.getAssignedTripTaxi(tripUuid);
    }

    @PostMapping()
    public Trip publishTrip(@Valid @RequestBody Trip trip) {
        return tripService.publishTrip(trip);
    }

    @PutMapping("{tripUuid}/taxi")
    public Trip updateTripTaxi(@PathVariable UUID tripUuid, @Valid @RequestBody Taxi taxi) {
        return tripService.updateTripTaxi(tripUuid, taxi);
    }

    @PutMapping("{tripUuid}/tripStatus")
    public Trip updateTripStatus(@PathVariable UUID tripUuid, @Valid @RequestBody TripStatus tripStatus) {
        return tripService.updateTripStatus(tripUuid, tripStatus);
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(TripNotFoundException ex) {
        log.error("TripEntity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TripEntity not found.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("TripEntity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong request body.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("An error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }

}
