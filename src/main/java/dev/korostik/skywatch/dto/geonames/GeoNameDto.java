package dev.korostik.skywatch.dto.geonames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoNameDto(@JsonProperty("name") String name, @JsonProperty("lat") String latitude,
                         @JsonProperty("lng") String longitude, @JsonProperty("countryName") String countryName,
                         @JsonProperty("adminName1") String adminName1, @JsonProperty("adminName2") String adminName2,
                         @JsonProperty("adminName3") String adminName3, @JsonProperty("adminName4") String adminName4,
                         @JsonProperty("adminName5") String adminName5, @JsonProperty("fcl") String featureClass,
                         @JsonProperty("fcode") String featureCode, @JsonProperty("timezone") TimeZoneDto timeZone) {
}
