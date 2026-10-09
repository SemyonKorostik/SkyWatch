package dev.korostik.skywatch.service;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.mapper.LocationMapper;
import dev.korostik.skywatch.repository.LocationRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

  @Value("${app.location.search-radius-meters}")
  private Integer SEARCH_RADIUS_METERS;

  private final LocationMapper locationMapper;
  private final LocationRepository locationRepository;

  public Optional<Location> getNearest(Double latitude, Double longitude) {
    return locationRepository.findNearestByLatitudeAndLongitude(latitude, longitude,
        SEARCH_RADIUS_METERS);
  }

  public Location map(GeoNameDto dto) {
    return locationMapper.mapToEntity(dto);
  }

  @Transactional
  public Location save(Location location) {
    return locationRepository.save(location);
  }

}
