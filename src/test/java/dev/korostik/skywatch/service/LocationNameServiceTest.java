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
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.location.LocationName;
import dev.korostik.skywatch.entities.location.LocationNameId;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.mapper.struct.LocationNameMapper;
import dev.korostik.skywatch.repository.LocationNameRepository;
import dev.korostik.skywatch.service.location.LocationNameServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationNameServiceTest {

  public static final double LAT = 53.9045;
  public static final double LON = 27.5615;
  public static final long LOCATION_ID = 1L;
  @Mock
  private LocationNameMapper locationNameMapper;

  @Mock
  private LocationNameRepository locationNameRepository;

  @InjectMocks
  private LocationNameServiceImpl locationNameService;

  @Test
  void findById_whenExists_thenReturnsLocationName() {
    LocationNameId id = new LocationNameId(LOCATION_ID, Language.RU);
    LocationName entity = new LocationName();
    when(locationNameRepository.findById(id)).thenReturn(Optional.of(entity));

    Optional<LocationName> result = locationNameService.findById(id);

    assertTrue(result.isPresent());
    assertSame(entity, result.get());
    verify(locationNameRepository).findById(id);
  }

  @Test
  void findById_whenNotFound_thenReturnsEmpty() {
    LocationNameId id = new LocationNameId(LOCATION_ID, Language.RU);
    when(locationNameRepository.findById(id)).thenReturn(Optional.empty());

    Optional<LocationName> result = locationNameService.findById(id);

    assertTrue(result.isEmpty());
    verify(locationNameRepository).findById(id);
  }

  @Test
  void mapToEntity_whenValidDtoAndLocation_thenSetsIdFromLocationAndLanguage() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", LAT, LON, "Belarus", "P", "PPL",
        new TimeZoneDto("Europe/Minsk")
    );
    Location location = new Location();
    location.setId(LOCATION_ID);
    Language language = Language.RU;

    LocationName mapped = new LocationName();
    when(locationNameMapper.mapToEntity(dto)).thenReturn(mapped);

    LocationName result = locationNameService.mapToEntity(dto, location, language);

    assertSame(mapped, result);
    assertEquals(new LocationNameId(LOCATION_ID, Language.RU), result.getId());
    verify(locationNameMapper).mapToEntity(dto);
  }

  @Test
  void mapToEntity_whenLocationNotPersisted_thenThrowsException() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", LAT, LON, "Belarus", "P", "PPL",
        new TimeZoneDto("Europe/Minsk")
    );
    Location location = new Location(); // id == null
    Language language = Language.RU;

    assertThrows(IllegalStateException.class,
        () -> locationNameService.mapToEntity(dto, location, language));

    verifyNoInteractions(locationNameMapper);
  }

  @Test
  void mapToEntity_whenLanguageIsNull_thenThrowsNullPointerException() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", LAT, LON, "Belarus", "P", "PPL",
        new TimeZoneDto("Europe/Minsk")
    );
    Location location = new Location();
    location.setId(LOCATION_ID);

    assertThrows(NullPointerException.class,
        () -> locationNameService.mapToEntity(dto, location, null));
  }

  @Test
  void save_whenCalled_thenDelegatesToRepository() {
    LocationName entity = new LocationName();
    LocationName saved = new LocationName();
    when(locationNameRepository.save(entity)).thenReturn(saved);

    LocationName result = locationNameService.save(entity);

    assertSame(saved, result);
    verify(locationNameRepository).save(entity);
  }
}
