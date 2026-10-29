package dev.korostik.skywatch.dto.location;

import java.time.ZoneId;

public record LocationNameDto(
    String country, String placeName, ZoneId timeZoneId
) {

}
