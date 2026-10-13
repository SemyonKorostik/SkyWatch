package dev.korostik.skywatch.dto.geonames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoNameDto(@JsonProperty("name") @NotNull String name,
                         @JsonProperty("lat") @NotNull @Range(min = -90, max = 90) Double latitude,
                         @JsonProperty("lng") @NotNull @Range(min = -180, max = 180) Double longitude,
                         @JsonProperty("countryName") @NotNull String countryName,
                         @JsonProperty("fcl") @NotNull String featureClass,
                         @JsonProperty("fcode") @NotNull String featureCode,
                         @JsonProperty("timezone") @NotNull TimeZoneDto timeZone) {

}
