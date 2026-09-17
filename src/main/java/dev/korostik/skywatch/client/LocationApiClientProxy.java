package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.GeonamesResponse;
import dev.korostik.skywatch.enums.GeoNamesSource;
import dev.korostik.skywatch.enums.GeoNamesStyle;
import dev.korostik.skywatch.enums.Language;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationApiClientProxy {
    @Value("${app.geonames.username}")
    private String userName;
    @Value("${app.geonames.style}")
    private GeoNamesStyle style;
    @Value("${app.geonames.inside-boundary}")
    private Boolean insideBoundary;
    @Value("${app.geonames.geo-name-source}")
    private GeoNamesSource geoNamesSource;
    @Value("${app.geonames.max-rows}")
    private Integer maxRowsCount;

    private final LocationApiClient locationApiClient;

    public Optional<GeoNameDto> getLocation(Double latitude, Double longitude, Language language) {
        return locationApiClient.getLocation(latitude, longitude, language, insideBoundary, style, geoNamesSource, maxRowsCount, userName)
            .geonames().stream().findFirst();
    }
}
