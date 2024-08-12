package com.rafaelwaldo.taxis.central.controller;

import com.rafaelwaldo.taxis.central.domain.Stats;
import com.rafaelwaldo.taxis.central.domain.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/central/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public Stats listStats() {
        return statsService.getStats();
    }
}
