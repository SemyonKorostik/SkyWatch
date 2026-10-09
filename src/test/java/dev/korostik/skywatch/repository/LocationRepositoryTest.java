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

 /* @Autowired
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

  @Test
  void findNearest_noPoints_returnsEmpty() {
    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(53.9045, 27.5615, 5000);
    assertFalse(result.isPresent());
  }

  @Test
  void findNearest_pointAtExactRadius_returnsIt() {
    // Create a point exactly 5000 meters away (approx 0.045 degrees latitude at equator)
    // For simplicity, we use a known point: (53.9045, 27.5615) and another point slightly offset.
    // We'll trust the PostGIS function; we just insert two points and query with radius that includes the second.
    Location center = Location.builder()
        .latitude(53.9045)
        .longitude(27.5615)
        .timeZoneId(ZoneId.of("Europe/Minsk"))
        .build();
    repository.save(center);

    // Point roughly 0.045 degrees north (~5 km)
    Location nearEdge = Location.builder()
        .latitude(53.9495) // 53.9045 + 0.045
        .longitude(27.5615)
        .timeZoneId(ZoneId.of("Europe/Minsk"))
        .build();
    repository.save(nearEdge);

    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(53.9045, 27.5615, 5000);
    assertTrue(result.isPresent());
    // Should return the center because it's closer (distance 0) than the edge (5000m)
    assertEquals(53.9045, result.get().getLatitude());
    assertEquals(27.5615, result.get().getLongitude());
  }

  @Test
  void findNearest_multipleSameDistance_returnsOne() {
    Location center = Location.builder()
        .latitude(53.9045)
        .longitude(27.5615)
        .timeZoneId(ZoneId.of("Europe/Minsk"))
        .build();
    repository.save(center);

    // Two points at same approximate distance (e.g., north and east)
    Location north = Location.builder()
        .latitude(53.9495)
        .longitude(27.5615)
        .timeZoneId(ZoneId.of("Europe/Minsk"))
        .build();
    Location east = Location.builder()
        .latitude(53.9045)
        .longitude(27.6065) // approx 0.045 degrees east
        .timeZoneId(ZoneId.of("Europe/Minsk"))
        .build();
    repository.save(north);
    repository.save(east);

    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(53.9045, 27.5615, 5000);
    assertTrue(result.isPresent());
    // The returned location should be one of the three (center, north, east). Since center distance 0, it will be center.
    // To avoid reliance on center, we can delete center and query again.
    repository.delete(center);
    result = repository.findNearestByLatitudeAndLongitude(53.9045, 27.5615, 5000);
    assertTrue(result.isPresent());
    double lat = result.get().getLatitude();
    double lon = result.get().getLongitude();
    // Accept either north or east
    boolean isNorth = Math.abs(lat - 53.9495) < 0.001 && Math.abs(lon - 27.5615) < 0.001;
    boolean isEast = Math.abs(lat - 53.9045) < 0.001 && Math.abs(lon - 27.6065) < 0.001;
    assertTrue(isNorth || isEast, "Returned location is not one of the expected points");
  }*/
}
