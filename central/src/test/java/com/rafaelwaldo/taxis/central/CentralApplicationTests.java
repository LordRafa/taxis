package com.rafaelwaldo.taxis.central;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CentralApplicationTests {

    @Test
    void contextLoads() {
        // Test if the context loads
    }

}
