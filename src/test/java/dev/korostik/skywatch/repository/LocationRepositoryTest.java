package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.entities.Location;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestContainerConfiguration.class)
class LocationRepositoryTest {

  @Autowired
  private LocationRepository repository;

  @Test
  void findNearestByLatitudeAndLongitude() {
    Location loc = Location.builder()
        .latitude(53.9045)
        .longitude(27.5615)
        .timeZoneId(ZoneId.of("Europe/Minsk"))
        .build();
    repository.save(loc);

    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(53.9045, 27.5615, 5000);

    assertTrue(result.isPresent());
    assertEquals(53.9045, result.get().getLatitude());
    assertEquals(27.5615, result.get().getLongitude());
  }

  @Test
  void findNearest_farAwayPoint_notReturned() {
    Location near = Location.builder()
        .latitude(53.9045).longitude(27.5615)
        .timeZoneId(ZoneId.of("Europe/Minsk")).build();
    repository.save(near);

    Location far = Location.builder()
        .latitude(0.0).longitude(0.0)
        .timeZoneId(ZoneId.of("UTC")).build();
    repository.save(far);

    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(53.9045, 27.5615, 5000);
    assertTrue(result.isPresent());
    assertEquals(53.9045, result.get().getLatitude());
  }
}
