package dev.korostik.skywatch.service;

import dev.korostik.skywatch.client.LocationApiClientProxy;
import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.entities.LocationNameId;
import dev.korostik.skywatch.enums.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//сервис нуждается в рефакторинге
public class LocationClientService {

  private final LocationService locationService;
  private final LocationNameService locationNameService;
  private final LocationApiClientProxy locationApiClientProxy;

  /**
   * Возвращает имя локации для заданных координат и языка.
   * Если локация ближайшая не найдена – создаёт новую локацию и её имя.
   * Если локация найдена, но имя на нужном языке отсутствует – создаёт только имя.
   */
  public LocationName findOrCreateLocationName(Double latitude, Double longitude, Language language) {
    return locationService.getNearest(latitude, longitude)
        .map(location -> {
          LocationNameId id = new LocationNameId(location.getId(), language);
          return locationNameService.findById(id)
              .orElseGet(() -> createLocationNameOnly(location, language));
        })
        .orElseGet(() -> createLocationAndName(latitude, longitude, language));
  }

  private LocationName createLocationAndName(Double latitude, Double longitude, Language language) {
    GeoNameDto dto = fetchLocationDto(latitude, longitude, language);
    Location mappedLocation = locationService.map(dto);
    Location savedLocation = locationService.save(mappedLocation);
    LocationName mappedLocationName = locationNameService.mapToEntity(dto, savedLocation, language);
    return locationNameService.save(mappedLocationName);
  }

  private LocationName createLocationNameOnly(Location location, Language language) {
    GeoNameDto dto = fetchLocationDto(location.getLatitude(), location.getLongitude(), language);
    LocationName mappedLocationName = locationNameService.mapToEntity(dto, location, language);
    return locationNameService.save(mappedLocationName);
  }

  private GeoNameDto fetchLocationDto(Double latitude, Double longitude, Language language) {
    return locationApiClientProxy.fetch(latitude, longitude, language)
        .orElseThrow(() -> new IllegalStateException(
            "Hey there! We couldn't find a location for those coordinates."));
  }
}
