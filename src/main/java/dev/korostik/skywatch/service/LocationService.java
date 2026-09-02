package dev.korostik.skywatch.service;

import dev.korostik.skywatch.client.LocationApiClientProxy;
import dev.korostik.skywatch.dto.geonames.GeonamesResponse;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.mapper.LocationMapper;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

  private final LocationApiClientProxy locationApiClientProxy;
  private final LocationMapper locationMapper;

  public Location getLocation(Float latitude, Float longitude, Language language) {
    return locationMapper.mapToEntity(
        Optional.ofNullable(locationApiClientProxy.getLocation(latitude, longitude, language))
            .map(GeonamesResponse::geonames)
            .stream()
            .flatMap(Collection::stream)
            .findFirst()
            .orElseThrow());
  }


}
