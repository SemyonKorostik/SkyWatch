package dev.korostik.skywatch.client;

import dev.korostik.skywatch.enums.GeoNamesSource;
import dev.korostik.skywatch.enums.GeoNamesStyle;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.dto.geonames.GeonamesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "locationApi", url = "http://api.geonames.org")
public interface LocationApiClient {
    @GetMapping("/findNearbyPlaceNameJSON")
    GeonamesResponse getLocation(@RequestParam("lat") Float latitude,
                                 @RequestParam("lng") Float longitude,
                                 @RequestParam("lang") Language language,
                                 @RequestParam("localCountry") Boolean insideBoundary,
                                 @RequestParam GeoNamesStyle style,
                                 @RequestParam("cities") GeoNamesSource geoNamesSource,
                                 @RequestParam("maxRowsCount") Integer maxRowsCount,
                                 @RequestParam String userName);
}
