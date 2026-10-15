package dev.korostik.skywatch.mapper.custom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.korostik.skywatch.dto.weather.external.DailyDataDto;
import dev.korostik.skywatch.dto.weather.transit.DailyData;
import dev.korostik.skywatch.mapper.MapperTestBase;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DailyDataMapperTest extends MapperTestBase {

    private final DailyDataMapper mapper = new DailyDataMapper();
    private final LocalDate date1 = LocalDate.of(2024, 1, 15);
    private final LocalDate date2 = LocalDate.of(2024, 1, 16);

    @Nested
    @DisplayName("toList (External DTO -> Transit List)")
    class ToListTests {

        @Test
        @DisplayName("Should unroll parallel lists into list of objects")
        void toList_happyPath_unrollsLists() {
            DailyDataDto dto = DailyDataDto.builder()
                    .time(List.of(date1, date2))
                    .temperatureMax(List.of(10.0, 12.0))
                    .temperatureMin(List.of(0.0, 2.0))
                    .weatherCode(List.of("0", "61"))
                    .build();

            List<DailyData> result = mapper.tolist(dto);

            assertThat(result).hasSize(2);
            
            assertThat(result.get(0))
                    .extracting(DailyData::date, DailyData::temperatureMax, DailyData::temperatureMin, DailyData::weatherCode)
                    .containsExactly(date1, 10.0, 0.0, "0");

            assertThat(result.get(1))
                    .extracting(DailyData::date, DailyData::temperatureMax, DailyData::temperatureMin, DailyData::weatherCode)
                    .containsExactly(date2, 12.0, 2.0, "61");
        }

        @Test
        @DisplayName("Should return empty list for empty input lists")
        void toList_emptyLists_returnsEmptyList() {
            DailyDataDto dto = DailyDataDto.builder()
                    .time(List.of())
                    .temperatureMax(List.of())
                    .temperatureMin(List.of())
                    .weatherCode(List.of())
                    .build();

            assertThat(mapper.tolist(dto)).isEmpty();
        }

        @Test
        @DisplayName("Should throw NPE if any list is null (IntStream.range on null)")
        void toList_nullList_throwsNPE() {
            DailyDataDto dto = DailyDataDto.builder()
                    .time(null) // null
                    .temperatureMax(List.of(1.0))
                    .temperatureMin(List.of(1.0))
                    .weatherCode(List.of("0"))
                    .build();

            assertThatNullPointerException().isThrownBy(() -> mapper.tolist(dto));
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException if lists have different sizes")
        void toList_mismatchedSizes_throwsException() {
            DailyDataDto dto = DailyDataDto.builder()
                    .time(List.of(date1, date2)) // 2 elements
                    .temperatureMax(List.of(10.0)) // 1 element
                    .temperatureMin(List.of(0.0, 1.0))
                    .weatherCode(List.of("0", "61"))
                    .build();

            assertThatThrownBy(() -> mapper.tolist(dto))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("toDto (Transit List -> External DTO)")
    class ToDtoTests {

        @Test
        @DisplayName("Should roll up list of objects into parallel lists")
        void toDto_happyPath_rollsUpLists() {
            List<DailyData> input = List.of(
                    DailyData.builder().date(date1).temperatureMax(10.0).temperatureMin(0.0).weatherCode("0").build(),
                    DailyData.builder().date(date2).temperatureMax(12.0).temperatureMin(2.0).weatherCode("61").build()
            );

            DailyDataDto dto = mapper.toDto(input);

            assertThat(dto.time()).containsExactly(date1, date2);
            assertThat(dto.temperatureMax()).containsExactly(10.0, 12.0);
            assertThat(dto.temperatureMin()).containsExactly(0.0, 2.0);
            assertThat(dto.weatherCode()).containsExactly("0", "61");
        }

        @Test
        @DisplayName("Should roll up into empty lists for empty input")
        void toDto_emptyList_returnsDtoWithEmptyLists() {
            DailyDataDto dto = mapper.toDto(List.of());
            assertThat(dto.time()).isEmpty();
            assertThat(dto.temperatureMax()).isEmpty();
            assertThat(dto.temperatureMin()).isEmpty();
            assertThat(dto.weatherCode()).isEmpty();
        }

        @Test
        @DisplayName("Should throw NPE on null input")
        void toDto_nullInput_throwsNPE() {
            assertThatNullPointerException().isThrownBy(() -> mapper.toDto(null));
        }
    }
}
