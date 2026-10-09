package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.GeonamesResponse;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.enums.GeoNamesSource;
import dev.korostik.skywatch.enums.GeoNamesStyle;
import dev.korostik.skywatch.enums.Language;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
class LocationApiClientProxyTest {

  @Mock
  private LocationApiClient locationApiClient;

  @InjectMocks
  private LocationApiClientProxy proxy;

  //  @Test
  void fetch() {
    ReflectionTestUtils.setField(proxy, "userName", "testuser");
    ReflectionTestUtils.setField(proxy, "style", GeoNamesStyle.FULL);
    ReflectionTestUtils.setField(proxy, "insideBoundary", true);
    ReflectionTestUtils.setField(proxy, "geoNamesSource", GeoNamesSource.CITIES_500);
    ReflectionTestUtils.setField(proxy, "maxRowsCount", 1);

    GeoNameDto dto = new GeoNameDto("Minsk", 53.9045, 27.5615, "Belarus", "P", "PPL",
        new TimeZoneDto(3, "Europe/Minsk", 3));
    GeonamesResponse<List<GeoNameDto>> response = new GeonamesResponse<>(List.of(dto));

    when(locationApiClient.fetch(53.9045, 27.5615, Language.EN, true, GeoNamesStyle.FULL,
        GeoNamesSource.CITIES_500, 1, "testuser")).thenReturn(response);

    Optional<GeoNameDto> result = proxy.fetch(53.9045, 27.5615, Language.EN);

    assertTrue(result.isPresent());
    assertEquals(dto, result.get());
    verify(locationApiClient).fetch(53.9045, 27.5615, Language.EN, true, GeoNamesStyle.FULL,
        GeoNamesSource.CITIES_500, 1, "testuser");
    verifyNoMoreInteractions(locationApiClient);
  }

}
