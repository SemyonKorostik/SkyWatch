package dev.korostik.skywatch;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.mapper.LocationMapper;
import dev.korostik.skywatch.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainerConfiguration.class)
class SkyWatchApplicationTests {

  @Autowired
  private LocationService locationService;

  @Autowired
  private LocationMapper locationMapper;

  @Test
  void contextLoads_whenApplicationStarts_thenLoadsSuccessfully() {
  }

  @Test
  void contextLoads_whenBeansInjected_thenNotNull() {
    assertNotNull(locationService);
    assertNotNull(locationMapper);
  }

}
