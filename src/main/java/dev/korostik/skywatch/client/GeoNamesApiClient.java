package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.LocationResponse;
import dev.korostik.skywatch.enums.GeoNamesSource;
import dev.korostik.skywatch.enums.GeoNamesStyle;
import java.util.List;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "locationApi", url = "http://api.geonames.org")
public interface GeoNamesApiClient {

  @GetMapping("/findNearbyPlaceNameJSON")
  Optional<LocationResponse<List<GeoNameDto>>> fetch(@RequestParam("lat") Double latitude,
      @RequestParam("lng") Double longitude,
      @RequestParam("lang") String language,
      @RequestParam("localCountry") Boolean insideBoundary,
      @RequestParam("style") GeoNamesStyle style,
      @RequestParam("cities") GeoNamesSource geoNamesSource,
      @RequestParam("maxRows") Integer maxRowsCount,
      @RequestParam("username") String userName);
}
