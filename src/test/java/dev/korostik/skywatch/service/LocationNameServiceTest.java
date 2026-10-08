package dev.korostik.skywatch.service;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.entities.LocationNameId;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.mapper.LocationNameMapper;
import dev.korostik.skywatch.repository.LocationNameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationNameServiceTest {

  @Mock
  private LocationNameMapper locationNameMapper;

  @Mock
  private LocationNameRepository locationNameRepository;

  @Mock
  private LocationService locationService;

  @InjectMocks
  private LocationNameService locationNameService;

  @Test
  void save_shouldPersistLocationNameWithCorrectId() {
    GeoNameDto dto = new GeoNameDto("Minsk", 53.9045, 27.5615, "Belarus", "P", "PPL",
        new TimeZoneDto(3, "Europe/Minsk", 3));
    Language language = Language.EN;

    Location location = new Location();
    location.setId(1L);

    LocationName mapped = new LocationName();
    mapped.setPlaceName("Minsk");
    mapped.setCountry("Belarus");

    LocationName saved = new LocationName();
    saved.setPlaceName("Minsk");
    saved.setCountry("Belarus");

    when(locationService.save(dto)).thenReturn(location);
    when(locationNameMapper.mapToEntity(dto)).thenReturn(mapped);
    when(locationNameRepository.save(mapped)).thenReturn(saved);

    LocationName result = locationNameService.save(dto, language);

    assertSame(saved, result);

    ArgumentCaptor<LocationName> captor = ArgumentCaptor.forClass(LocationName.class);
    verify(locationNameRepository).save(captor.capture());
    LocationName persisted = captor.getValue();

    assertEquals(1L, persisted.getId().getLocationId());
    assertEquals(language.name(), persisted.getId().getLanguage());
    assertEquals("Minsk", persisted.getPlaceName());
    assertEquals("Belarus", persisted.getCountry());

    verify(locationService).save(dto);
    verify(locationNameMapper).mapToEntity(dto);
  }

  @Test
  void save_throwsWhenLocationServiceFails() {
    GeoNameDto dto = new GeoNameDto("Minsk", 53.9045, 27.5615, "Belarus", "P", "PPL",
        new TimeZoneDto(3, "Europe/Minsk", 3));
    when(locationService.save(dto)).thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> locationNameService.save(dto, Language.EN));
    verify(locationNameRepository, never()).save(any());
  }
}
