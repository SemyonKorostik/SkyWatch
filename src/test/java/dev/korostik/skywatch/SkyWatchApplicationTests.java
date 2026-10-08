package dev.korostik.skywatch;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.mapper.LocationMapper;
import dev.korostik.skywatch.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(TestContainerConfiguration.class)
class SkyWatchApplicationTests {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationMapper locationMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void contextLoads_withBeans() {
        assertNotNull(locationService);
        assertNotNull(locationMapper);
    }

}
