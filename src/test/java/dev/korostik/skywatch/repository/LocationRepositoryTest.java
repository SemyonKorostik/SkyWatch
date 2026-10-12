package dev.korostik.skywatch.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.entities.Location;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(TestContainerConfiguration.class)
class LocationRepositoryTest {

  public static final double LAT = 53.9045;
  public static final double LON = 27.5615;
  public static final int RADIUS = 5000;
  public static final double RADIUS_IN_DEGREES = 0.044;
  public static final String EUROPE_MINSK = "Europe/Minsk";

  @Autowired
  private LocationRepository repository;

  private Location createLocation(double lat, double lon, String timeZone) {
    return Location.builder()
        .latitude(lat)
        .longitude(lon)
        .timeZoneId(ZoneId.of(timeZone))
        .build();
  }

  @Test
  void findNearestByLatitudeAndLongitude_whenExactMatchExists_thenReturnsIt() {
    Location loc = createLocation(LAT, LON, EUROPE_MINSK);
    repository.save(loc);

    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(LAT, LON, RADIUS);

    assertTrue(result.isPresent());
    assertEquals(LAT, result.get().getLatitude());
    assertEquals(LON, result.get().getLongitude());
  }

  @Test
  void findNearestByLatitudeAndLongitude_whenOnlyFarAwayPointExists_thenReturnsEmpty() {
    Location far = createLocation(0.0, 0.0, "UTC");
    repository.save(far);

    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(LAT, LON, RADIUS);
    assertFalse(result.isPresent());
  }

  @Test
  void findNearestByLatitudeAndLongitude_whenNoPoints_thenReturnsEmpty() {
    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(LAT, LON, RADIUS);
    assertFalse(result.isPresent());
  }

  @Test
  void findNearestByLatitudeAndLongitude_whenMultiplePoints_thenReturnsNearestFromCenter() {
    Location nearEdge = createLocation(LAT + RADIUS_IN_DEGREES, LON, EUROPE_MINSK);
    repository.save(nearEdge);

    Location nearCenter = createLocation(LAT + RADIUS_IN_DEGREES / 2, LON, EUROPE_MINSK);
    repository.save(nearCenter);

    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(LAT, LON, RADIUS);
    assertTrue(result.isPresent());
    assertEquals(nearCenter.getLatitude(), result.get().getLatitude());
    assertEquals(nearCenter.getLongitude(), result.get().getLongitude());
  }

  @Test
  void findNearestByLatitudeAndLongitude_whenBoundaryPointExists_thenReturnsIt() {
    Location nearEdge = createLocation(LAT + RADIUS_IN_DEGREES, LON, EUROPE_MINSK);
    repository.save(nearEdge);

    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(LAT, LON, RADIUS);
    assertTrue(result.isPresent());
    assertEquals(nearEdge.getLatitude(), result.get().getLatitude());
    assertEquals(nearEdge.getLongitude(), result.get().getLongitude());
  }

  @Test
  void findNearestByLatitudeAndLongitude_whenMultipleSameDistance_thenReturnsOne() {
    Location north = createLocation(LAT + RADIUS_IN_DEGREES, LON, EUROPE_MINSK);
    Location east = createLocation(LAT, LON + RADIUS_IN_DEGREES, EUROPE_MINSK);
    repository.save(north);
    repository.save(east);

    Optional<Location> result = repository.findNearestByLatitudeAndLongitude(LAT, LON, RADIUS);

    assertTrue(result.isPresent());
  }
}
