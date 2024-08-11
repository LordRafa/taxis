package com.rafaelwaldo.taxis.central.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.central.TestcontainersConfiguration;
import com.rafaelwaldo.taxis.central.domain.Taxi;
import com.rafaelwaldo.taxis.central.domain.TaxiCommand;
import com.rafaelwaldo.taxis.central.domain.Trip;
import com.rafaelwaldo.taxis.central.domain.TripStatus;
import com.rafaelwaldo.taxis.central.mapper.TaxiMapper;
import com.rafaelwaldo.taxis.central.repository.TaxiRepository;
import com.rafaelwaldo.taxis.central.repository.TripRepository;
import com.rafaelwaldo.taxis.central.repository.entity.TaxiEntity;
import com.rafaelwaldo.taxis.central.repository.entity.TripEntity;
import com.rafaelwaldo.taxis.central.utils.MockHelper;
import com.rafaelwaldo.taxis.central.utils.RabbitMQTestConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.rafaelwaldo.taxis.central.domain.TaxiCommandName.PUBLISH_TRIP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class TripControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private RabbitMQTestConsumer rabbitMQTestConsumer;

    @Autowired
    private TaxiRepository taxiRepository;

    @Autowired
    private TaxiMapper taxiMapper;

    @BeforeEach
    void setUp() {
        tripRepository.deleteAll();
    }

    @Test
    void publishTrip() throws Exception {

        Trip trip = MockHelper.getMockTrip().uuid(null).build();

        mockMvc.perform(post("/central/trip/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trip)))
                .andExpect(status().isOk())
                .andDo(result -> compareOriginalTrip(result, trip));

        boolean messageReceived = rabbitMQTestConsumer.getLatch().await(10, TimeUnit.SECONDS);
        assertThat(messageReceived).isTrue();
        TaxiCommand taxiCommand = objectMapper.readValue(rabbitMQTestConsumer.getReceivedMessage(), TaxiCommand.class);
        assertThat(taxiCommand.taxiCommandName()).isEqualTo(PUBLISH_TRIP);

    }

    private void compareOriginalTrip(MvcResult result, Trip trip) throws JsonProcessingException, UnsupportedEncodingException {
        Trip tripSaved = objectMapper.readValue(result.getResponse().getContentAsString(), Trip.class);
        assertThat(tripSaved.uuid()).isNotNull();
        assertThat(tripSaved.location()).isEqualTo(trip.location());
        assertThat(tripSaved.tripStatus()).isEqualTo(trip.tripStatus());
        assertThat(tripSaved.taxi()).isNull();
    }

    @Test
    void getAssignedTripTaxi() throws Exception {
        TaxiEntity taxiEntity = MockHelper.getMockTaxiEntity().uuid(null).build();
        TaxiEntity taxiSaved = taxiRepository.save(taxiEntity);
        Taxi taxi = taxiMapper.toDomain(taxiSaved);

        TripEntity tripEntity = MockHelper.getMockTripEntity().uuid(null).taxi(taxiSaved).build();
        TripEntity tripSaved = tripRepository.save(tripEntity);

        mockMvc.perform(get("/central/trip/" + tripSaved.getUuid() + "/taxi"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taxi)));
    }

    @Test
    void getAssignedTripTaxiNotFound() throws Exception {

        UUID tripUuid = UUID.randomUUID();

        mockMvc.perform(get("/central/trip/" + tripUuid + "/taxi"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTripTaxi() throws Exception {
        TaxiEntity taxiEntity = MockHelper.getMockTaxiEntity().uuid(null).build();
        TaxiEntity taxiSaved = taxiRepository.save(taxiEntity);
        Taxi taxi = taxiMapper.toDomain(taxiSaved);

        TripEntity tripEntity = MockHelper.getMockTripEntity().uuid(null).build();
        TripEntity tripSaved = tripRepository.save(tripEntity);

        mockMvc.perform(put("/central/trip/" + tripSaved.getUuid() + "/taxi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taxi)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Trip updatedTrip = objectMapper.readValue(result.getResponse().getContentAsString(), Trip.class);
                    assertThat(updatedTrip.taxi()).isEqualTo(taxi);
                });
    }

    @Test
    void updateTripStatus() throws Exception {
        TripEntity tripEntity = MockHelper.getMockTripEntity().uuid(null).build();
        TripEntity tripSaved = tripRepository.save(tripEntity);

        TripStatus newStatus = TripStatus.COMPLETED;

        mockMvc.perform(put("/central/trip/" + tripSaved.getUuid() + "/tripStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStatus)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Trip updatedTrip = objectMapper.readValue(result.getResponse().getContentAsString(), Trip.class);
                    assertThat(updatedTrip.tripStatus()).isEqualTo(newStatus);
                });
    }
}