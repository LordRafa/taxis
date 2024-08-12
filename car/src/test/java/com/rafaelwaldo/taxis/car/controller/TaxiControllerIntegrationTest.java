package com.rafaelwaldo.taxis.car.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.car.domain.Taxi;
import com.rafaelwaldo.taxis.car.domain.TaxiStatus;
import com.rafaelwaldo.taxis.car.domain.Trip;
import com.rafaelwaldo.taxis.car.domain.TripStatus;
import com.rafaelwaldo.taxis.car.domain.pojo.TaxiPojo;
import com.rafaelwaldo.taxis.car.mapper.TaxiMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static com.rafaelwaldo.taxis.car.utils.MockHelper.getMockTaxi;
import static com.rafaelwaldo.taxis.car.utils.MockHelper.getMockTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaxiControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    TaxiMapper taxiMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private TaxiPojo taxiPojo;

    @BeforeEach
    void setUp() {
        when(restTemplate.postForEntity(eq("http://localhost:8080/central/taxi"), any(), any()))
                .thenReturn(new ResponseEntity<>(getMockTaxi().build(), HttpStatus.OK));
    }

    @Test
    void testCompleteCarTrip() throws Exception {

        Trip trip = getMockTrip().tripStatus(TripStatus.ASSIGNED).build();
        taxiPojo.setTaxiStatus(TaxiStatus.BOOKED);
        taxiPojo.setCurrentTrip(trip);

        Taxi expectedTaxi = taxiMapper.toDomain(taxiPojo)
                .withTaxiStatus(TaxiStatus.AVAILABLE)
                .withCurrentTripUuid(null);

        RequestEntity<TripStatus> requestEntity = RequestEntity
                .put("http://localhost:8080/central/trip/" + trip.uuid() + "/tripStatus")
                .body(TripStatus.COMPLETED);
        when(restTemplate.exchange(requestEntity, Trip.class))
                .thenReturn(new ResponseEntity<>(trip, HttpStatus.OK));

        mockMvc.perform(put("/taxi/taxi/trip/complete"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTaxi)));

        assertThat(taxiPojo.getCurrentTrip()).isNull();
        assertThat(taxiPojo.getTaxiStatus()).isEqualTo(TaxiStatus.AVAILABLE);
    }

}
