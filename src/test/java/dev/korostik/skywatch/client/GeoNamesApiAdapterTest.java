package dev.korostik.skywatch.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GeoNamesApiAdapterTest {

  private GeoNamesApiClient client;
  private GeoNamesApiAdapter adapter;

  @BeforeEach
  void setUp() {
    client = Mockito.mock(GeoNamesApiClient.class);
    adapter = new GeoNamesApiAdapter("testuser", GeoNamesStyle.FULL, true,
        GeoNamesSource.CITIES_500, 5, client);
  }

  @Nested
  @DisplayName("fetch")
  class FetchTests {

    @Test
    @DisplayName("Should return first GeoNameDto when response is present")
    void fetch_happyPath_returnsDto() {
      GeoNameDto dto = new GeoNameDto("Minsk", 53.9, 27.56, "Belarus", "P", "PPL",
          new TimeZoneDto("Europe/Minsk"));
      LocationResponse<List<GeoNameDto>> resp = new LocationResponse<>(List.of(dto));
      Mockito.when(client.fetch(53.9, 27.56, "EN", true, GeoNamesStyle.FULL,
              GeoNamesSource.CITIES_500, 5, "testuser"))
          .thenReturn(Optional.of(resp));

      var result = adapter.fetch(53.9, 27.56, Language.EN);

      assertThat(result).isSameAs(dto);
    }

    @Test
    @DisplayName("Should throw when response is empty")
    void fetch_emptyResponse_throwsException() {
      Mockito.when(client.fetch(Mockito.anyDouble(), Mockito.anyDouble(),
              Mockito.anyString(), Mockito.anyBoolean(), Mockito.any(),
              Mockito.any(), Mockito.anyInt(), Mockito.anyString()))
          .thenReturn(Optional.empty());

      assertThatThrownBy(() -> adapter.fetch(53.9, 27.56, Language.EN))
          .isInstanceOf(NoSuchElementException.class);
    }
  }
}
