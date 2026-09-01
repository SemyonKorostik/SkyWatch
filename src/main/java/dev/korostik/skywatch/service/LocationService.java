package dev.korostik.skywatch.service;

import dev.korostik.skywatch.client.LocationApiClientProxy;
import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.dto.geonames.GeonamesResponse;
import dev.korostik.skywatch.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

//    private final LocationApiClientProxy locationApiClientProxy;
//    private final LocationRepository locationRepository;

/*    public List<GeoNameDto> getLocation(String latitude, String longitude, Language language) {
        Optional.ofNullable(locationApiClientProxy.getLocation(latitude, longitude, language))
                .map(GeonamesResponse::geonames)
                .stream()
                .flatMap(Collection::stream)
                .findFirst()
                .orElseThrow();

    }*/
}
