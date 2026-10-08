package dev.korostik.skywatch.service;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.mapper.LocationMapper;
import dev.korostik.skywatch.repository.LocationRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

  @Mock
  private LocationMapper locationMapper;

  @Mock
  private LocationRepository locationRepository;

  @InjectMocks
  private LocationService locationService;

  @Test
  void save() {
    GeoNameDto dto = new GeoNameDto("Minsk", 53.9045, 27.5615, "Belarus", "P", "PPL",
        new TimeZoneDto(3, "Europe/Minsk", 3));
    Location entity = new Location();
    Location saved = new Location();
    when(locationMapper.mapToEntity(dto)).thenReturn(entity);
    when(locationRepository.save(entity)).thenReturn(saved);

    Location result = locationService.save(dto);

    assertEquals(saved, result);
    verify(locationMapper).mapToEntity(dto);
    verify(locationRepository).save(entity);
    verifyNoMoreInteractions(locationMapper, locationRepository);
  }

  @Test
  void getNearest() {
    ReflectionTestUtils.setField(locationService, "SEARCH_RADIUS_METERS", 5000);
    Location loc = new Location();
    when(locationRepository.findNearestByLatitudeAndLongitude(53.9045, 27.5615, 5000))
        .thenReturn(Optional.of(loc));

    Optional<Location> result = locationService.getNearest(53.9045, 27.5615);

    assertTrue(result.isPresent());
    assertEquals(loc, result.get());
    verify(locationRepository).findNearestByLatitudeAndLongitude(53.9045, 27.5615, 5000);
    verifyNoMoreInteractions(locationMapper, locationRepository);
  }
}
