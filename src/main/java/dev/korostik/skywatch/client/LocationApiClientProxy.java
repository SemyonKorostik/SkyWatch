package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.enums.GeoNamesSource;
import dev.korostik.skywatch.enums.GeoNamesStyle;
import dev.korostik.skywatch.enums.Language;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@Validated
public class LocationApiClientProxy {

  private final String userName;
  private final GeoNamesStyle style;
  private final Boolean insideBoundary;
  private final GeoNamesSource geoNamesSource;
  private final Integer maxRowsCount;

  private final LocationApiClient locationApiClient;

  public LocationApiClientProxy(@Value("${app.geonames.username}") String userName,
      @Value("${app.geonames.style}") GeoNamesStyle style,
      @Value("${app.geonames.inside-boundary}") Boolean insideBoundary,
      @Value("${app.geonames.geo-name-source}") GeoNamesSource geoNamesSource,
      @Value("${app.geonames.max-rows}") Integer maxRowsCount,
      LocationApiClient locationApiClient) {
    this.userName = userName;
    this.style = style;
    this.insideBoundary = insideBoundary;
    this.geoNamesSource = geoNamesSource;
    this.maxRowsCount = maxRowsCount;
    this.locationApiClient = locationApiClient;
  }

  public Optional<@Valid GeoNameDto> fetch(@NotNull Double latitude, @NotNull Double longitude,
      @NotNull Language language) {
    return locationApiClient.fetch(latitude, longitude, language.name(), insideBoundary, style,
            geoNamesSource, maxRowsCount, userName)
        .flatMap(x -> x.geonames().stream().findFirst());
  }
}
