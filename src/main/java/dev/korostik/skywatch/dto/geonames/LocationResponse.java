package dev.korostik.skywatch.dto.geonames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LocationResponse<T>(@JsonProperty("geonames") T geonames) {

}
