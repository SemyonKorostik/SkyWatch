package dev.korostik.skywatch.mapper.custom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.korostik.skywatch.dto.weather.external.HourlyDataDto;
import dev.korostik.skywatch.dto.weather.transit.HourlyData;
import dev.korostik.skywatch.mapper.MapperTestBase;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HourlyDataMapperTest extends MapperTestBase {

    private final HourlyDataMapper mapper = new HourlyDataMapper();
    private final LocalDateTime time1 = LocalDateTime.of(2024, 1, 15, 12, 0);
    private final LocalDateTime time2 = LocalDateTime.of(2024, 1, 15, 13, 0);

    @Nested
    @DisplayName("toList (External DTO -> Transit List)")
    class ToListTests {

        @Test
        @DisplayName("Should unroll all parallel lists into list of objects")
        void toList_happyPath_unrollsAllFields() {
            HourlyDataDto dto = HourlyDataDto.builder()
                    .time(List.of(time1, time2))
                    .temperature(List.of(5.0, 4.0))
                    .relativeHumidity(List.of(80, 85))
                    .weatherCode(List.of("0", "61"))
                    .apparentTemperature(List.of(3.0, 2.0))
                    .precipitation(List.of(0.0, 1.5))
                    .precipitationProbability(List.of(0, 90))
                    .windSpeed(List.of(2.0, 5.0))
                    .windDirection(List.of(180, 270))
                    .surfacePressure(List.of(1010.0, 1005.0))
                    .build();

            List<HourlyData> result = mapper.toList(dto);

            assertThat(result).hasSize(2);
            
            HourlyData h1 = result.get(0);
            assertThat(h1.time()).isEqualTo(time1);
            assertThat(h1.temperature()).isEqualTo(5.0);
            assertThat(h1.relativeHumidity()).isEqualTo(80);
            assertThat(h1.weatherCode()).isEqualTo("0");
            assertThat(h1.apparentTemperature()).isEqualTo(3.0);
            assertThat(h1.precipitation()).isEqualTo(0.0);
            assertThat(h1.precipitationProbability()).isEqualTo(0);
            assertThat(h1.windSpeed()).isEqualTo(2.0);
            assertThat(h1.windDirection()).isEqualTo(180);
            assertThat(h1.surfacePressure()).isEqualTo(1010.0);

            HourlyData h2 = result.get(1);
            assertThat(h2.time()).isEqualTo(time2);
            assertThat(h2.weatherCode()).isEqualTo("61");
            assertThat(h2.precipitation()).isEqualTo(1.5);
        }

        @Test
        void toList_emptyLists_returnsEmptyList() {
            HourlyDataDto dto = emptyDto();
            assertThat(mapper.toList(dto)).isEmpty();
        }

        @Test
        void toList_nullList_throwsNPE() {
            HourlyDataDto dto = emptyDto();
            HourlyDataDto badDto = new HourlyDataDto(
                    null, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of()
            );
            assertThatNullPointerException().isThrownBy(() -> mapper.toList(badDto));
        }

        @Test
        void toList_mismatchedSizes_throwsIndexOutOfBounds() {
            HourlyDataDto dto = new HourlyDataDto(
                    List.of(time1, time2), // 2
                    List.of(1.0),          // 1 -> mismatch
                    List.of(1), List.of("0"), List.of(1.0), List.of(1.0), List.of(1), List.of(1.0), List.of(1), List.of(1.0)
            );
            assertThatThrownBy(() -> mapper.toList(dto))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        private HourlyDataDto emptyDto() {
            return new HourlyDataDto(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
        }
    }

    @Nested
    @DisplayName("toDto (Transit List -> External DTO)")
    class ToDtoTests {

        @Test
        @DisplayName("Should roll up all fields into parallel lists preserving order")
        void toDto_happyPath_rollsUpAllFields() {
            List<HourlyData> input = List.of(
                    HourlyData.builder().time(time1).temperature(5.0).relativeHumidity(80).weatherCode("0")
                            .apparentTemperature(3.0).precipitation(0.0).precipitationProbability(0)
                            .windSpeed(2.0).windDirection(180).surfacePressure(1010.0).build(),
                    HourlyData.builder().time(time2).temperature(4.0).relativeHumidity(85).weatherCode("61")
                            .apparentTemperature(2.0).precipitation(1.5).precipitationProbability(90)
                            .windSpeed(5.0).windDirection(270).surfacePressure(1005.0).build()
            );

            HourlyDataDto dto = mapper.toDto(input);

            assertThat(dto.time()).containsExactly(time1, time2);
            assertThat(dto.temperature()).containsExactly(5.0, 4.0);
            assertThat(dto.relativeHumidity()).containsExactly(80, 85);
            assertThat(dto.weatherCode()).containsExactly("0", "61");
            assertThat(dto.apparentTemperature()).containsExactly(3.0, 2.0);
            assertThat(dto.precipitation()).containsExactly(0.0, 1.5);
            assertThat(dto.precipitationProbability()).containsExactly(0, 90);
            assertThat(dto.windSpeed()).containsExactly(2.0, 5.0);
            assertThat(dto.windDirection()).containsExactly(180, 270);
            assertThat(dto.surfacePressure()).containsExactly(1010.0, 1005.0);
        }

        @Test
        void toDto_emptyList_returnsDtoWithEmptyLists() {
            HourlyDataDto dto = mapper.toDto(List.of());
            assertThat(dto.time()).isEmpty();
            assertThat(dto.temperature()).isEmpty();
        }

        @Test
        void toDto_nullInput_throwsNPE() {
            assertThatNullPointerException().isThrownBy(() -> mapper.toDto(null));
        }
    }
}
