package dev.korostik.skywatch.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.LocationResponse;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.enums.GeoNamesSource;
import dev.korostik.skywatch.enums.GeoNamesStyle;
import dev.korostik.skywatch.enums.Language;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationApiClientProxyTest {

  public static final double LAT = 53.9045;
  public static final double LON = 27.5615;
  @Mock
  private GeoNamesApiClient locationApiClient;

  private GeoNamesApiAdapter proxy;

  @BeforeEach
  void setUp() {
    proxy = new GeoNamesApiAdapter("testuser", GeoNamesStyle.FULL, true,
        GeoNamesSource.CITIES_500, 1, locationApiClient);
  }

  @Test
  void fetch_whenClientReturnsData_thenReturnsFirstGeoName() {
    GeoNameDto dto = new GeoNameDto("Minsk", LAT, LON, "Belarus", "P", "PPL",
        new TimeZoneDto("Europe/Minsk"));
    Optional<LocationResponse<List<GeoNameDto>>> response = Optional.of(
        new LocationResponse<>(List.of(dto)));

    when(locationApiClient.fetch(LAT, LON, Language.EN.name(), true, GeoNamesStyle.FULL,
        GeoNamesSource.CITIES_500, 1, "testuser")).thenReturn(response);

    GeoNameDto result = proxy.fetch(LAT, LON, Language.EN);

    assertSame(dto, result);
    verify(locationApiClient).fetch(LAT, LON, Language.EN.name(), true, GeoNamesStyle.FULL,
        GeoNamesSource.CITIES_500, 1, "testuser");
  }

  @Test
  void fetch_whenClientReturnsEmptyList_thenReturnsEmpty() {
    Optional<LocationResponse<List<GeoNameDto>>> response = Optional.of(
        new LocationResponse<>(List.of()));

    when(locationApiClient.fetch(LAT, LON, Language.EN.name(), true, GeoNamesStyle.FULL,
        GeoNamesSource.CITIES_500, 1, "testuser")).thenReturn(response);

    assertThrows(NoSuchElementException.class, () -> proxy.fetch(LAT, LON, Language.EN));
    verify(locationApiClient).fetch(LAT, LON, Language.EN.name(), true, GeoNamesStyle.FULL,
        GeoNamesSource.CITIES_500, 1, "testuser");
  }

}
