package dev.korostik.skywatch.dto.geonames;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TimeZoneDto(@JsonProperty("gmtOffset") Integer gmtOffset, @JsonProperty("timeZoneId") String timeZoneId,
                          @JsonProperty("dstOffset") Integer dstOffset) {

}
