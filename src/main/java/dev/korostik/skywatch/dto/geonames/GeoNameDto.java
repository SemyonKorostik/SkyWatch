package dev.korostik.skywatch.dto.geonames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoNameDto(@JsonProperty("name") String name, @JsonProperty("lat") BigDecimal latitude,
                         @JsonProperty("lng") BigDecimal longitude, @JsonProperty("countryName") String countryName,
                         @JsonProperty("adminName1") String adminName1, @JsonProperty("adminName2") String adminName2,
                         @JsonProperty("adminName3") String adminName3, @JsonProperty("adminName4") String adminName4,
                         @JsonProperty("adminName5") String adminName5, @JsonProperty("fcl") String featureClass,
                         @JsonProperty("fcode") String featureCode, @JsonProperty("timezone") TimeZoneDto timeZone) {
}
