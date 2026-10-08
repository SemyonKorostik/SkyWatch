package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.entities.LocationNameId;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestContainerConfiguration.class)
class LocationNameRepositoryTest {

  @Autowired
  private LocationNameRepository locationNameRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Test
  void saveAndExists() {
    Location location = Location.builder()
        .latitude(53.9045)
        .longitude(27.5615)
        .timeZoneId(ZoneId.of("Europe/Minsk"))
        .build();
    location = locationRepository.save(location);

    LocationName name = LocationName.builder()
        .id(new LocationNameId(location.getId(), "EN"))
        .location(location)
        .country("Belarus")
        .placeName("Minsk")
        .build();
    locationNameRepository.save(name);

    assertTrue(locationNameRepository.existsById(new LocationNameId(location.getId(), "EN")));
  }

  @Test
  void save_duplicateLocationName_throws() {
    Location location = Location.builder()
        .latitude(53.9045)
        .longitude(27.5615)
        .timeZoneId(ZoneId.of("Europe/Minsk"))
        .build();
    location = locationRepository.save(location);

    LocationName name = LocationName.builder()
        .id(new LocationNameId(location.getId(), "EN"))
        .location(location)
        .country("Belarus")
        .placeName("Minsk")
        .build();
    locationNameRepository.save(name);

    LocationName duplicate = LocationName.builder()
        .id(new LocationNameId(location.getId(), "EN"))
        .location(location)
        .country("Belarus")
        .placeName("Minsk")
        .build();

    assertThrows(DataIntegrityViolationException.class, () -> locationNameRepository.save(duplicate));
  }

  @Test
  void existsById_false() {
    assertFalse(locationNameRepository.existsById(new LocationNameId(999L, "EN")));
  }

  @Test
  void deleteLocationName() {
    Location location = Location.builder()
        .latitude(53.9045).longitude(27.5615)
        .timeZoneId(ZoneId.of("Europe/Minsk")).build();
    location = locationRepository.save(location);

    LocationName name = LocationName.builder()
        .id(new LocationNameId(location.getId(), "EN"))
        .location(location).country("Belarus").placeName("Minsk").build();
    locationNameRepository.save(name);

    locationNameRepository.delete(name);
    assertFalse(locationNameRepository.existsById(name.getId()));
  }
  
}
