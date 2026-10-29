package dev.korostik.skywatch.client;

import static org.assertj.core.api.Assertions.assertThat;

import dev.korostik.skywatch.dto.weather.external.feignclient.ForecastRequest;
import dev.korostik.skywatch.dto.weather.external.feignclient.ForecastResponse;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OpenMeteoApiAdapterTest {

  private OpenMeteoApiClient apiClient;
  private OpenMeteoApiAdapter adapter;

  @BeforeEach
  void setUp() {
    apiClient = Mockito.mock(OpenMeteoApiClient.class);
    adapter = new OpenMeteoApiAdapter(apiClient);
  }

  @Nested
  @DisplayName("getForecast")
  class GetForecastTests {

    @Test
    @DisplayName("Should delegate to OpenMeteoApiClient")
    void getForecast_delegatesToClient() {
      ForecastRequest req = ForecastRequest.builder()
          .latitude(53.9).longitude(27.56).forecastDays(3)
          .zoneId("UTC")
          .build();
      ForecastResponse expected = ForecastResponse.builder()
          .latitude(53.9)
          .longitude(27.56)
          .zoneId(ZoneId.of("Europe/Minsk"))
          .build();
      Mockito.when(apiClient.getForecast(53.9, 27.56, null, null, null, "UTC", 3))
          .thenReturn(expected);

      ForecastResponse result = adapter.getForecast(req);

      assertThat(result).isSameAs(expected);
    }
  }

  @Test
  @DisplayName("getType should return OPEN_METEO")
  void getType_returnsOpenMeteo() {
    assertThat(adapter.getType()).isEqualTo(ForecastApiProvider.OPEN_METEO);
  }
}
