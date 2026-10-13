package dev.korostik.skywatch.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.entities.LocationNameId;
import dev.korostik.skywatch.enums.Language;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(TestContainerConfiguration.class)
class LocationNameRepositoryTest {

  @Autowired
  private LocationNameRepository locationNameRepository;

  @Autowired
  private LocationRepository locationRepository;

  private Location createAndSaveLocation() {
    Location location = Location.builder()
        .latitude(53.9045)
        .longitude(27.5615)
        .timeZoneId(ZoneId.of("Europe/Minsk"))
        .build();
    return locationRepository.save(location);
  }

  private LocationName createLocationName(Location location, Language language, String country, String placeName) {
    return LocationName.builder()
        .id(new LocationNameId(location.getId(), language))
        .location(location)
        .country(country)
        .placeName(placeName)
        .build();
  }

  @Test
  void save_whenValidEntity_thenExistsInRepository() {
    Location location = createAndSaveLocation();

    LocationName name = createLocationName(location, Language.EN, "Belarus", "Minsk");
    locationNameRepository.save(name);

    assertTrue(
        locationNameRepository.existsById(new LocationNameId(location.getId(), Language.EN)));
  }

  @Test
  void save_whenDuplicateId_thenUpdatesLocationName() {
    Location location = createAndSaveLocation();

    LocationName name = createLocationName(location, Language.EN, "Wrong country", "Wrong place name");
    locationNameRepository.save(name);

    LocationName duplicate = createLocationName(location, Language.EN, "Belarus", "Minsk");
    locationNameRepository.save(duplicate);

    List<LocationName> locationNames = locationNameRepository.findByLocationId(location.getId());
    assertEquals(1, locationNames.size());
    assertEquals(duplicate, locationNames.getFirst());
  }

  @Test
  void existsById_whenNotExists_thenReturnsFalse() {
    assertFalse(locationNameRepository.existsById(new LocationNameId(999L, Language.EN)));
  }

  @Test
  void delete_whenPersistedEntity_thenRemovesFromRepository() {
    Location location = createAndSaveLocation();

    LocationName name = createLocationName(location, Language.EN, "Belarus", "Minsk");
    locationNameRepository.save(name);

    locationNameRepository.delete(name);
    assertFalse(locationNameRepository.existsById(name.getId()));
  }

  @Test
  void save_whenNullLocation_thenThrowsDataIntegrityViolation() {
    LocationName name = LocationName.builder()
        .id(new LocationNameId(null, Language.EN))
        .location(null)
        .country("Belarus")
        .placeName("Minsk")
        .build();

    assertThrows(JpaSystemException.class, () -> locationNameRepository.save(name));
  }

  @Test
  void save_whenNullLanguage_thenThrowsDataIntegrityViolation() {
    LocationName name = LocationName.builder()
        .id(new LocationNameId(123L, null))
        .location(null)
        .country("Belarus")
        .placeName("Minsk")
        .build();

    assertThrows(JpaSystemException.class, () -> locationNameRepository.save(name));
  }

  @Test
  void save_whenDifferentLanguagesForSameLocation_thenSucceeds() {
    Location location = createAndSaveLocation();

    LocationName nameEn = createLocationName(location, Language.EN, "Belarus", "Minsk");
    LocationName savedEn = locationNameRepository.save(nameEn);

    assertNotNull(savedEn.getId());
    assertEquals(Language.EN, savedEn.getId().getLanguage());
    assertEquals("Belarus", savedEn.getCountry());
    assertEquals("Minsk", savedEn.getPlaceName());

    LocationName nameRu = createLocationName(location, Language.RU, "Беларусь", "Минск");
    LocationName savedRu = locationNameRepository.save(nameRu);

    assertNotNull(savedRu.getId());
    assertEquals(Language.RU, savedRu.getId().getLanguage());
    assertEquals("Беларусь", savedRu.getCountry());
    assertEquals("Минск", savedRu.getPlaceName());

    List<LocationName> locationNames = locationNameRepository.findByLocationId(location.getId());

    assertEquals(2, locationNames.size());
    assertTrue(locationNames.contains(savedEn));
    assertTrue(locationNames.contains(savedRu));
  }
}
