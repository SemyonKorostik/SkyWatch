package dev.korostik.skywatch.dto.geonames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeonamesResponse<T>(@JsonProperty("geonames") T geonames) {
}
