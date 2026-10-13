package dev.korostik.skywatch.service.location;

import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.enums.Language;

public interface LocationClientService {

  LocationName findOrCreateLocationName(Double latitude, Double longitude, Language language);
}
