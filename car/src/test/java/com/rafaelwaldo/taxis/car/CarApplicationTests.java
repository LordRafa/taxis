package com.rafaelwaldo.taxis.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.rafaelwaldo.taxis.car.utils.MockHelper.getMockTaxi;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CarApplicationTests {

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        when(restTemplate.postForEntity(eq("http://localhost:8080/central/taxi"), any(), any()))
                .thenReturn(new ResponseEntity<>(getMockTaxi().build(), HttpStatus.OK));
    }

    @Test
    void contextLoads() {
        // Test if the context loads
    }

}
