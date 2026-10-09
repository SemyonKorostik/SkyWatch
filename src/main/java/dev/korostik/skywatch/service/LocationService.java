package dev.korostik.skywatch.service;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.mapper.LocationMapper;
import dev.korostik.skywatch.repository.LocationRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

  private final int searchRadiusMeters;
  private final LocationMapper locationMapper;
  private final LocationRepository locationRepository;

  public LocationService(LocationMapper locationMapper, LocationRepository locationRepository,
      @Value("${app.location.search-radius-meters}") int searchRadiusMeters) {
    this.locationMapper = locationMapper;
    this.locationRepository = locationRepository;
    this.searchRadiusMeters = searchRadiusMeters;
  }

  public Optional<Location> getNearest(Double latitude, Double longitude) {
    if (searchRadiusMeters <= 0) {
      throw new IllegalArgumentException("Search radius must be greater than 0");
    }
    return locationRepository.findNearestByLatitudeAndLongitude(latitude, longitude,
        searchRadiusMeters);
  }

  public Location map(GeoNameDto dto) {
    return locationMapper.mapToEntity(dto);
  }

  public Location save(Location location) {
    return locationRepository.save(location);
  }

}
