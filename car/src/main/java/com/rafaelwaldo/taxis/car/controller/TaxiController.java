package com.rafaelwaldo.taxis.car.controller;

import com.rafaelwaldo.taxis.car.domain.Taxi;
import com.rafaelwaldo.taxis.car.domain.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taxi")
@RequiredArgsConstructor
public class TaxiController {

    private final CarService carService;

    @PutMapping("/taxi/trip/complete")
    public ResponseEntity<Taxi> completeCarTrip() {
        Taxi taxi = carService.completeCarTrip();
        return ResponseEntity.ok().body(taxi);
    }
}
