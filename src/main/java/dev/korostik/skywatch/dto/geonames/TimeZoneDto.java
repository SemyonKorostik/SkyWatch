package dev.korostik.skywatch.dto.geonames;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record TimeZoneDto(@JsonProperty("timeZoneId") @NotNull String timeZoneId) {

}
