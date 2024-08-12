package com.rafaelwaldo.taxis.central.controller;

import com.rafaelwaldo.taxis.central.TestcontainersConfiguration;
import com.rafaelwaldo.taxis.central.domain.TaxiStatus;
import com.rafaelwaldo.taxis.central.domain.TripStatus;
import com.rafaelwaldo.taxis.central.repository.TaxiRepository;
import com.rafaelwaldo.taxis.central.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static com.rafaelwaldo.taxis.central.utils.MockHelper.getMockTaxiEntity;
import static com.rafaelwaldo.taxis.central.utils.MockHelper.getMockTripEntity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class StatsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaxiRepository taxiRepository;

    @Autowired
    private TripRepository tripRepository;

    @BeforeEach
    void setUp() {
        taxiRepository.deleteAll();
        tripRepository.deleteAll();

        taxiRepository.save(getMockTaxiEntity().taxiStatus(TaxiStatus.AVAILABLE).build());
        taxiRepository.save(getMockTaxiEntity().taxiStatus(TaxiStatus.AVAILABLE).build());
        taxiRepository.save(getMockTaxiEntity().taxiStatus(TaxiStatus.BOOKED).build());
        taxiRepository.save(getMockTaxiEntity().taxiStatus(TaxiStatus.BOOKED).build());

        tripRepository.save(getMockTripEntity().tripStatus(TripStatus.PENDING).build());
        tripRepository.save(getMockTripEntity().tripStatus(TripStatus.PENDING).build());
        tripRepository.save(getMockTripEntity().tripStatus(TripStatus.ASSIGNED).build());
        tripRepository.save(getMockTripEntity().tripStatus(TripStatus.COMPLETED).build());
        tripRepository.save(getMockTripEntity().tripStatus(TripStatus.COMPLETED).build());
        tripRepository.save(getMockTripEntity().tripStatus(TripStatus.CANCELED).build());
    }

    @Test
    void testListStats() throws Exception {
        mockMvc.perform(get("/central/stats"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"taxiStats\":{\"totalTaxis\":4,\"totalAvailableTaxis\":2,\"totalBookedTaxis\":2},\"tripStats\":{\"totalTrips\":6,\"totalPendingTrips\":2,\"totalAssignedTrips\":1,\"totalCompletedTrips\":2,\"totalCanceledTrips\":1}}"));
    }
}