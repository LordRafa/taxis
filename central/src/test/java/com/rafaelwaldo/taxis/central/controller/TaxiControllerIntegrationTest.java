package com.rafaelwaldo.taxis.central.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.central.TestcontainersConfiguration;
import com.rafaelwaldo.taxis.central.domain.Taxi;
import com.rafaelwaldo.taxis.central.repository.TaxiRepository;
import com.rafaelwaldo.taxis.central.repository.entity.TaxiEntity;
import com.rafaelwaldo.taxis.central.utils.MockHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class TaxiControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaxiRepository taxiRepository;

    @BeforeEach
    void setUp() {
        taxiRepository.deleteAll();
    }

    @Test
    void listTaxis() throws Exception {
        TaxiEntity taxiEntity = MockHelper.getMockTaxiEntity().build();
        taxiEntity = taxiRepository.save(taxiEntity);

        Taxi taxi = MockHelper.getMockTaxi().uuid(taxiEntity.getUuid()).build();
        String expected = objectMapper.writeValueAsString(new Taxi[]{taxi});

        mockMvc.perform(get("/central/taxis"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void publishTaxis() throws Exception {
        Taxi taxi = MockHelper.getMockTaxi().build();
        String taxiJson = objectMapper.writeValueAsString(taxi);

        mockMvc.perform(post("/central/taxis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taxiJson))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Taxi returnedTaxi = objectMapper.readValue(result.getResponse().getContentAsString(), Taxi.class);
                    assertThat(returnedTaxi).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(taxi);
                    assertThat(returnedTaxi.uuid()).isNotNull();
                });
    }
}
