package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.geonames.GeonamesResponse;
import dev.korostik.skywatch.enums.GeoNamesSource;
import dev.korostik.skywatch.enums.GeoNamesStyle;
import dev.korostik.skywatch.enums.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class LocationApiClientProxy {
    @Value("${geonames.username}")
    private String userName;
    @Value("${geonames.style}")
    private GeoNamesStyle style;
    @Value("insideBoundary")
    private Boolean insideBoundary;
    @Value("geoNameSource")
    private GeoNamesSource geoNamesSource;
    @Value("maxRows")
    private Integer maxRowsCount;

    private final LocationApiClient locationApiClient;

    public GeonamesResponse getLocation(String latitude, String longitude, Language language) {
        return locationApiClient.getLocation(latitude, longitude, language, insideBoundary, style, geoNamesSource, maxRowsCount, userName);
    }
}
