package dev.korostik.skywatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.mapper.LocationMapper;
import dev.korostik.skywatch.repository.LocationRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

  private static final double LAT = 53.9045;
  private static final double LON = 27.5615;
  private static final int RADIUS = 5000;

  @Mock
  private LocationMapper locationMapper;

  @Mock
  private LocationRepository locationRepository;

  private LocationService locationService;

  @BeforeEach
  void setUp() {
    locationService = new LocationService(locationMapper, locationRepository, RADIUS);
  }

  @Test
  void save_success() {
    Location entity = new Location();
    Location saved = new Location();
    when(locationRepository.save(entity)).thenReturn(saved);

    Location result = locationService.save(entity);

    assertEquals(saved, result);
    verify(locationRepository).save(entity);
  }

  @Test
  void getNearest_delegatesToRepository() {

    Location loc = new Location();
    when(locationRepository.findNearestByLatitudeAndLongitude(LAT, LON, RADIUS))
        .thenReturn(Optional.of(loc));

    Optional<Location> result = locationService.getNearest(LAT, LON);

    assertTrue(result.isPresent());
    assertEquals(loc, result.get());
    verify(locationRepository).findNearestByLatitudeAndLongitude(LAT, LON, RADIUS);
  }

  @Test
  void getNearest_returnsEmptyWhenNoLocation() {

    when(locationRepository.findNearestByLatitudeAndLongitude(LAT, LON, RADIUS))
        .thenReturn(Optional.empty());

    Optional<Location> result = locationService.getNearest(LAT, LON);

    assertFalse(result.isPresent());
    verify(locationRepository).findNearestByLatitudeAndLongitude(LAT, LON, RADIUS);
  }

  @Test
  void getNearest_throwExceptionWhenRadiusZero() {
    locationService = new LocationService(locationMapper, locationRepository, 0);

    assertThrows(IllegalArgumentException.class, () -> locationService.getNearest(LAT, LON));
    verifyNoInteractions(locationRepository);
  }

  @Test
  void getNearest_throwExceptionWhenRadiusNegative() {
    locationService = new LocationService(locationMapper, locationRepository, -1);

    assertThrows(IllegalArgumentException.class, () -> locationService.getNearest(LAT, LON));
    verifyNoInteractions(locationRepository);
  }

  @Test
  void map_returnsEntityFromMapper() {
    GeoNameDto dto = new GeoNameDto("Minsk", LAT, LON, "Belarus", "P", "PPL",
        new TimeZoneDto(3, "Europe/Minsk", 3));

    Location entity = new Location();

    when(locationMapper.mapToEntity(dto)).thenReturn(entity);

    Location result = locationService.map(dto);

    assertSame(entity, result);
    verify(locationMapper).mapToEntity(dto);
  }
}
