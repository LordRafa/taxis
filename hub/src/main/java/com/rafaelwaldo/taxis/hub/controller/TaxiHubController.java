package com.rafaelwaldo.taxis.hub.controller;

import com.rafaelwaldo.taxis.hub.domain.dto.RequestTaxiDTO;
import com.rafaelwaldo.taxis.hub.domain.dto.TaxiDTO;
import com.rafaelwaldo.taxis.hub.domain.exception.TripNotFoundException;
import com.rafaelwaldo.taxis.hub.domain.service.TaxiHubService;
import com.rafaelwaldo.taxis.hub.mapper.TaxiMapper;
import com.rafaelwaldo.taxis.hub.mapper.TripMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/hub/taxis")
@RequiredArgsConstructor
public class TaxiHubController {

    private final TaxiHubService taxiHubService;
    private final TripMapper tripMapper;
    private final TaxiMapper taxiMapper;

    @GetMapping
    public List<TaxiDTO> listTaxis() {
        return taxiHubService.listTaxis().stream().map(taxiMapper::toTaxiDTO).toList();
    }

    @PostMapping("/request")
    public TaxiDTO requestTaxi(@Valid @RequestBody RequestTaxiDTO requestTaxiDTO) {
        return taxiMapper.toTaxiDTO(taxiHubService.requestTaxi(tripMapper.toTrip(requestTaxiDTO)));
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(TripNotFoundException ex) {
        log.error("Trip not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip not found.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Trip not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong request body.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("An error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }

}
