package com.rafaelwaldo.taxis.central.controller;

import com.rafaelwaldo.taxis.central.domain.Taxi;
import com.rafaelwaldo.taxis.central.domain.service.TaxiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/central/taxis")
@RequiredArgsConstructor
public class TaxiController {

    private final TaxiService taxiService;

    @GetMapping
    public List<Taxi> listTaxis() {
        return taxiService.listTaxis();
    }

    @PostMapping
    public Taxi publishTaxis(@Valid @RequestBody Taxi taxi) {
        return taxiService.publishTaxi(taxi);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("An error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }

}
