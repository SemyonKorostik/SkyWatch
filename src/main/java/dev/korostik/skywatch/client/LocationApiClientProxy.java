package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.GeonamesResponse;
import dev.korostik.skywatch.enums.GeoNamesSource;
import dev.korostik.skywatch.enums.GeoNamesStyle;
import dev.korostik.skywatch.enums.Language;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationApiClientProxy {
    @Value("${geonames.username}")
    private String userName;
    @Value("${geonames.style}")
    private GeoNamesStyle style;
    @Value("${geonames.inside-boundary}")
    private Boolean insideBoundary;
    @Value("${geonames.geo-name-source}")
    private GeoNamesSource geoNamesSource;
    @Value("${geonames.max-rows}")
    private Integer maxRowsCount;

    private final LocationApiClient locationApiClient;

    public GeonamesResponse<List<GeoNameDto>> getLocation(Float latitude, Float longitude, Language language) {
        GeonamesResponse<List<GeoNameDto>> location = locationApiClient.getLocation(latitude, longitude, language, insideBoundary, style, geoNamesSource, maxRowsCount, userName);
        log.info(location.toString());
        return location;
    }
}
