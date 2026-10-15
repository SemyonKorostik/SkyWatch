package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.reference.WeatherProvider;
import dev.korostik.skywatch.entities.reference.WmoWeatherCode;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import dev.korostik.skywatch.mapper.custom.DailyDataMapper;
import dev.korostik.skywatch.mapper.custom.HourlyDataMapper;
import dev.korostik.skywatch.mapper.struct.CurrentWeatherMapper;
import dev.korostik.skywatch.mapper.struct.DailyWeatherMapper;
import dev.korostik.skywatch.mapper.struct.HourlyWeatherMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import org.mapstruct.factory.Mappers;

public abstract class MapperTestBase {

    protected static final ZoneId TEST_ZONE_ID = ZoneId.of("Europe/Minsk"); // UTC+3 (no DST in tests)
    protected static final Long TEST_LOCATION_ID = 1L;
    protected static final Double TEST_LAT = 53.9;
    protected static final Double TEST_LON = 27.56;
    
    protected static final String PROVIDER_CODE_CLEAR = "0";
    protected static final String PROVIDER_CODE_RAIN = "61";
    protected static final String PROVIDER_CODE_SNOW = "71";

    protected final CurrentWeatherMapper currentWeatherMapper = Mappers.getMapper(CurrentWeatherMapper.class);
    protected final DailyWeatherMapper dailyWeatherMapper = Mappers.getMapper(DailyWeatherMapper.class);
    protected final HourlyWeatherMapper hourlyWeatherMapper = Mappers.getMapper(HourlyWeatherMapper.class);
    protected final DailyDataMapper dailyDataMapper = new DailyDataMapper();
    protected final HourlyDataMapper hourlyDataMapper = new HourlyDataMapper();

    /**
     * Creates a test location with fixed ID and ZoneId.
     */
    protected Location createTestLocation() {
        return Location.builder()
                .id(TEST_LOCATION_ID)
                .latitude(TEST_LAT)
                .longitude(TEST_LON)
                .timeZoneId(TEST_ZONE_ID)
                .build();
    }

    /**
     * Creates a WeatherCondition map keyed by providerCode.
     * Mappers resolve conditions using this providerCode.
     */
    protected Map<String, WeatherCondition> createWeatherConditionMap() {
        Map<String, WeatherCondition> map = new HashMap<>();
        
        WeatherProvider provider = WeatherProvider.builder()
                .provider(ForecastApiProvider.OPEN_METEO)
                .build();

        WmoWeatherCode wmoClear = WmoWeatherCode.builder().code("000").description("Clear sky")
            .build();
        WmoWeatherCode wmoRain = WmoWeatherCode.builder().code("061").description("Rain").build();
        WmoWeatherCode wmoSnow = WmoWeatherCode.builder().code("071").description("Snow fall")
            .build();

        map.put(PROVIDER_CODE_CLEAR, WeatherCondition.builder()
                .id(10L)
                .providerName(provider)
                .providerCode(PROVIDER_CODE_CLEAR)
                .wmoCode(wmoClear)
                .build());
        
        map.put(PROVIDER_CODE_RAIN, WeatherCondition.builder()
                .id(11L)
                .providerName(provider)
                .providerCode(PROVIDER_CODE_RAIN)
                .wmoCode(wmoRain)
                .build());

        map.put(PROVIDER_CODE_SNOW, WeatherCondition.builder()
                .id(12L)
                .providerName(provider)
                .providerCode(PROVIDER_CODE_SNOW)
                .wmoCode(wmoSnow)
                .build());

        return map;
    }

    /**
     * Converts LocalDateTime to Instant using ZoneId (Hourly mapper logic).
     */
    protected Instant toInstant(LocalDateTime ldt, ZoneId zone) {
        return ldt.atZone(zone).toInstant();
    }

    /**
     * Converts LocalDate to Instant (start of day) using ZoneId (Daily mapper logic).
     */
    protected Instant toInstant(LocalDate date, ZoneId zone) {
        return date.atStartOfDay(zone).toInstant();
    }
}
