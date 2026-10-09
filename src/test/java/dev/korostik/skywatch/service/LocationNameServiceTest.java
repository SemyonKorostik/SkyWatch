package dev.korostik.skywatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.entities.LocationNameId;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.mapper.LocationNameMapper;
import dev.korostik.skywatch.repository.LocationNameRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationNameServiceTest {

  @Mock
  private LocationNameMapper locationNameMapper;

  @Mock
  private LocationNameRepository locationNameRepository;

  @InjectMocks
  private LocationNameService locationNameService;

  @Test
  void findById_returnsLocationName_whenExists() {
    LocationNameId id = new LocationNameId(1L, Language.RU);
    LocationName entity = new LocationName();
    when(locationNameRepository.findById(id)).thenReturn(Optional.of(entity));

    Optional<LocationName> result = locationNameService.findById(id);

    assertTrue(result.isPresent());
    assertSame(entity, result.get());
    verify(locationNameRepository).findById(id);
  }

  @Test
  void findById_returnsEmpty_whenNotFound() {
    LocationNameId id = new LocationNameId(1L, Language.RU);
    when(locationNameRepository.findById(id)).thenReturn(Optional.empty());

    Optional<LocationName> result = locationNameService.findById(id);

    assertTrue(result.isEmpty());
    verify(locationNameRepository).findById(id);
  }

  @Test
  void mapToEntity_setsIdFromLocationAndLanguage() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", 53.9045, 27.5615, "Belarus", "P", "PPL",
        new TimeZoneDto(3, "Europe/Minsk", 3)
    );
    Location location = new Location();
    location.setId(42L);
    Language language = Language.RU;

    LocationName mapped = new LocationName();
    when(locationNameMapper.mapToEntity(dto)).thenReturn(mapped);

    LocationName result = locationNameService.mapToEntity(dto, location, language);

    assertSame(mapped, result);
    assertEquals(new LocationNameId(42L, Language.RU), result.getId());
    verify(locationNameMapper).mapToEntity(dto);
  }

  @Test
  void mapToEntity_throws_whenLocationNotPersisted() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", 53.9045, 27.5615, "Belarus", "P", "PPL",
        new TimeZoneDto(3, "Europe/Minsk", 3)
    );
    Location location = new Location(); // id == null
    Language language = Language.RU;

    assertThrows(IllegalStateException.class,
        () -> locationNameService.mapToEntity(dto, location, language));

    verifyNoInteractions(locationNameMapper);
  }

  @Test
  void mapToEntity_throws_whenLanguageIsNull() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", 53.9045, 27.5615, "Belarus", "P", "PPL",
        new TimeZoneDto(3, "Europe/Minsk", 3)
    );
    Location location = new Location();
    location.setId(1L);

    assertThrows(NullPointerException.class,
        () -> locationNameService.mapToEntity(dto, location, null));
  }

  @Test
  void save_delegatesToRepository() {
    LocationName entity = new LocationName();
    LocationName saved = new LocationName();
    when(locationNameRepository.save(entity)).thenReturn(saved);

    LocationName result = locationNameService.save(entity);

    assertSame(saved, result);
    verify(locationNameRepository).save(entity);
  }
}
