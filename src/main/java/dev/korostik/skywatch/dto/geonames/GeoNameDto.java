package dev.korostik.skywatch.dto.geonames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoNameDto(@JsonProperty("name") String name,
                         @JsonProperty("lat") Double latitude,
                         @JsonProperty("lng") Double longitude,
                         @JsonProperty("countryName") String countryName,
                         @JsonProperty("fcl") String featureClass,
                         @JsonProperty("fcode") String featureCode,
                         @JsonProperty("timezone") TimeZoneDto timeZone) {

}
