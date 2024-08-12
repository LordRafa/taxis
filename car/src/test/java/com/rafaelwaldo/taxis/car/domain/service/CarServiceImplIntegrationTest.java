package com.rafaelwaldo.taxis.car.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelwaldo.taxis.car.TestcontainersConfiguration;
import com.rafaelwaldo.taxis.car.config.TaxiConfig;
import com.rafaelwaldo.taxis.car.domain.Taxi;
import com.rafaelwaldo.taxis.car.domain.TaxiCommand;
import com.rafaelwaldo.taxis.car.domain.TaxiCommandName;
import com.rafaelwaldo.taxis.car.domain.TaxiStatus;
import com.rafaelwaldo.taxis.car.domain.Trip;
import com.rafaelwaldo.taxis.car.domain.pojo.TaxiPojo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.rafaelwaldo.taxis.car.utils.MockHelper.getMockTaxi;
import static com.rafaelwaldo.taxis.car.utils.MockHelper.getMockTrip;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CarServiceImplIntegrationTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TaxiPojo taxiPojo;
    @Autowired
    private TaxiConfig taxiConfig;

    @BeforeEach
    void setUp() {
        when(restTemplate.postForEntity(eq("http://localhost:8080/central/taxi"), any(), any()))
                .thenReturn(new ResponseEntity<>(getMockTaxi().build(), HttpStatus.OK));
    }

    @Test
    void receiveTaxiCommand() throws Exception {
        Taxi taxi = getMockTaxi().build();
        Trip trip = getMockTrip().taxi(taxi).build();
        ResponseEntity<Trip> responseEntity = new ResponseEntity<>(trip, HttpStatus.OK);
        RequestEntity<Taxi> requestEntity = RequestEntity
                .put("http://localhost:8080/central/trip/" + trip.uuid() + "/taxi")
                .body(taxi);
        when(restTemplate.exchange(requestEntity, Trip.class)).thenReturn(responseEntity);

        TaxiCommand taxiCommand = new TaxiCommand(TaxiCommandName.PUBLISH_TRIP, objectMapper.writeValueAsString(trip));

        taxiPojo.setUuid(taxi.uuid());
        taxiPojo.setTaxiStatus(taxi.taxiStatus());
        taxiPojo.setPlate(taxi.plate());
        taxiPojo.setLocation(taxi.location());

        rabbitTemplate.convertAndSend(taxiConfig.getCommand().getExchange(), "", objectMapper.writeValueAsString(taxiCommand));

        await().atMost(2, SECONDS).untilAsserted(() -> {
            assertThat(taxiPojo.getCurrentTrip()).isEqualTo(trip);
            assertThat(taxiPojo.getTaxiStatus()).isEqualTo(TaxiStatus.BOOKED);
        });
    }
}