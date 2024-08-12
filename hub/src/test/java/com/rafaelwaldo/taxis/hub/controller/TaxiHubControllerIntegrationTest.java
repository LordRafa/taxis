package com.rafaelwaldo.taxis.hub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.hub.domain.Taxi;
import com.rafaelwaldo.taxis.hub.domain.Trip;
import com.rafaelwaldo.taxis.hub.domain.dto.RequestTaxiDTO;
import com.rafaelwaldo.taxis.hub.domain.dto.TaxiDTO;
import com.rafaelwaldo.taxis.hub.mapper.TaxiMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static com.rafaelwaldo.taxis.hub.utils.MockHelper.getMockRequestTaxiDTO;
import static com.rafaelwaldo.taxis.hub.utils.MockHelper.getMockTaxi;
import static com.rafaelwaldo.taxis.hub.utils.MockHelper.getMockTrip;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaxiHubControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    TaxiMapper taxiMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testListTaxis() throws Exception {
        Taxi taxi = getMockTaxi().build();

        when(restTemplate.getForEntity("http://localhost:8080/central/taxi", Taxi[].class))
                .thenReturn(new ResponseEntity<>(new Taxi[]{taxi}, HttpStatus.OK));

        mockMvc.perform(get("/hub/taxi"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new TaxiDTO[]{taxiMapper.toTaxiDTO(taxi)})));
    }

    @Test
    void testRequestTaxiSuccessAfter2Retries() throws Exception {
        RequestTaxiDTO requestTaxiDTO = getMockRequestTaxiDTO().build();
        Taxi taxi = getMockTaxi().build();
        Trip trip = getMockTrip().uuid(null).build();
        Trip tripWithUUid = getMockTrip().build();

        Mockito.reset(restTemplate);

        when(restTemplate.postForEntity("http://localhost:8080/central/trip", trip, Trip.class))
                .thenReturn(new ResponseEntity<>(tripWithUUid, HttpStatus.OK));

        when(restTemplate.getForEntity("http://localhost:8080/central/trip/" + tripWithUUid.uuid() + "/taxi", Taxi.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND))
                .thenReturn(new ResponseEntity<>(taxi, HttpStatus.OK));

        mockMvc.perform(post("/hub/taxi/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestTaxiDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taxiMapper.toTaxiDTO(taxi))));
    }

    @Test
    void testRequestTaxiErrorInvalidLocation() throws Exception {
        RequestTaxiDTO requestTaxiDTO = getMockRequestTaxiDTO().location(null).build();

        mockMvc.perform(post("/hub/taxi/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestTaxiDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Wrong request body."));
    }

    @Test
    void testRequestTaxiTripNotFound() throws Exception {
        RequestTaxiDTO requestTaxiDTO = getMockRequestTaxiDTO().build();
        Trip trip = getMockTrip().uuid(null).build();
        Trip tripWithUUid = getMockTrip().build();

        Mockito.reset(restTemplate);

        when(restTemplate.postForEntity("http://localhost:8080/central/trip", trip, Trip.class))
                .thenReturn(new ResponseEntity<>(tripWithUUid, HttpStatus.OK));

        when(restTemplate.getForEntity("http://localhost:8080/central/trip/" + tripWithUUid.uuid() + "/taxi", Taxi.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/hub/taxi/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestTaxiDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Trip not found."));
    }

    @Test
    void testRequestTaxiCentralError() throws Exception {
        RequestTaxiDTO requestTaxiDTO = getMockRequestTaxiDTO().build();
        Trip trip = getMockTrip().uuid(null).build();
        Taxi taxi = getMockTaxi().build();
        Trip tripWithUUid = getMockTrip().build();

        Mockito.reset(restTemplate);

        when(restTemplate.postForEntity("http://localhost:8080/central/trip", trip, Trip.class))
                .thenReturn(new ResponseEntity<>(tripWithUUid, HttpStatus.OK));

        when(restTemplate.getForEntity("http://localhost:8080/central/trip/" + tripWithUUid.uuid() + "/taxi", Taxi.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR))
                .thenReturn(new ResponseEntity<>(taxi, HttpStatus.OK));

        mockMvc.perform(post("/hub/taxi/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestTaxiDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("An unexpected error occurred."));
    }

}