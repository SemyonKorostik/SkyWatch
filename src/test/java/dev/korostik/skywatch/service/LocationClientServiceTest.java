package dev.korostik.skywatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import dev.korostik.skywatch.client.GeoNamesApiAdapter;
import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.location.LocationName;
import dev.korostik.skywatch.entities.location.LocationNameId;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.repository.LocationNameRepository;
import dev.korostik.skywatch.repository.LocationRepository;
import dev.korostik.skywatch.service.location.LocationClientServiceImpl;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(dev.korostik.skywatch.config.TestContainerConfiguration.class)
@ExtendWith(SpringExtension.class)
class LocationClientServiceTest {

    private static final String TEST_PLACE_NAME = "Minsk";
    private static final Double TEST_LAT = 53.9045;
    private static final Double TEST_LON = 27.5615;
    private static final String TEST_COUNTRY = "Belarus";
    private static final String TEST_TIMEZONE_ID = "Europe/Minsk";
    private static final Language TEST_LANGUAGE = Language.EN;

    @MockitoBean
    private GeoNamesApiAdapter locationApiClientProxy;

    @Autowired
    private LocationClientServiceImpl locationClientService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationNameRepository locationNameRepository;

    private GeoNameDto createTestGeoNameDto() {
        return new GeoNameDto(
            TEST_PLACE_NAME,
            TEST_LAT,
            TEST_LON,
            TEST_COUNTRY,
            "P",
            "PPL",
            new TimeZoneDto(TEST_TIMEZONE_ID)
        );
    }

    private Location createTestLocation() {
        return Location.builder()
            .latitude(TEST_LAT)
            .longitude(TEST_LON)
            .timeZoneId(ZoneId.of(TEST_TIMEZONE_ID))
            .build();
    }

    @Test
    void findOrCreateLocationName_whenLocationAndNameExist_thenReturnsExisting() {
        GeoNameDto dto = createTestGeoNameDto();
        when(locationApiClientProxy.fetch(eq(TEST_LAT), eq(TEST_LON), eq(TEST_LANGUAGE)))
            .thenReturn(dto);

        Location savedLocation = locationRepository.save(createTestLocation());
        LocationName savedName = locationNameRepository.save(
            new LocationName(new LocationNameId(savedLocation.getId(), TEST_LANGUAGE), savedLocation, TEST_COUNTRY, TEST_PLACE_NAME)
        );

        LocationName result = locationClientService.findOrCreateLocationName(TEST_LAT, TEST_LON, TEST_LANGUAGE);

        assertNotNull(result);
        assertEquals(savedName.getId(), result.getId());
        assertEquals(TEST_PLACE_NAME, result.getPlaceName());
        verify(locationApiClientProxy, never()).fetch(anyDouble(), anyDouble(), any());
    }

    @Test
    void findOrCreateLocationName_whenLocationExistsButNameMissing_thenCreatesNameOnly() {
        GeoNameDto dto = createTestGeoNameDto();
        when(locationApiClientProxy.fetch(eq(TEST_LAT), eq(TEST_LON), eq(TEST_LANGUAGE)))
            .thenReturn(dto);

        Location savedLocation = locationRepository.save(createTestLocation());

        LocationName result = locationClientService.findOrCreateLocationName(TEST_LAT, TEST_LON, TEST_LANGUAGE);

        assertNotNull(result);
        assertEquals(savedLocation.getId(), result.getLocation().getId());
        assertEquals(TEST_PLACE_NAME, result.getPlaceName());
        assertEquals(TEST_LANGUAGE, result.getId().getLanguage());
        verify(locationApiClientProxy).fetch(TEST_LAT, TEST_LON, TEST_LANGUAGE);
    }

    @Test
    void findOrCreateLocationName_whenLocationMissing_thenCreatesLocationAndName() {
        GeoNameDto dto = createTestGeoNameDto();
        when(locationApiClientProxy.fetch(eq(TEST_LAT), eq(TEST_LON), eq(TEST_LANGUAGE)))
            .thenReturn(dto);

        LocationName result = locationClientService.findOrCreateLocationName(TEST_LAT, TEST_LON, TEST_LANGUAGE);

        assertNotNull(result);
        assertNotNull(result.getLocation().getId());
        assertEquals(TEST_LAT, result.getLocation().getLatitude());
        assertEquals(TEST_LON, result.getLocation().getLongitude());
        assertEquals(TEST_PLACE_NAME, result.getPlaceName());
        verify(locationApiClientProxy).fetch(TEST_LAT, TEST_LON, TEST_LANGUAGE);
    }

    @Test
    void findOrCreateLocationName_whenDifferentLanguageForSameLocation_thenCreatesNewName() {
        GeoNameDto dtoEn = createTestGeoNameDto();
        GeoNameDto dtoRu = new GeoNameDto(
            "Минск",
            TEST_LAT,
            TEST_LON,
            "Беларусь",
            "P",
            "PPL",
            new TimeZoneDto(TEST_TIMEZONE_ID)
        );

        when(locationApiClientProxy.fetch(eq(TEST_LAT), eq(TEST_LON), eq(Language.EN)))
            .thenReturn(dtoEn);
        when(locationApiClientProxy.fetch(eq(TEST_LAT), eq(TEST_LON), eq(Language.RU)))
            .thenReturn(dtoRu);

        LocationName nameEn = locationClientService.findOrCreateLocationName(TEST_LAT, TEST_LON, Language.EN);
        LocationName nameRu = locationClientService.findOrCreateLocationName(TEST_LAT, TEST_LON, Language.RU);

        assertNotNull(nameEn);
        assertNotNull(nameRu);
        assertEquals(nameEn.getLocation().getId(), nameRu.getLocation().getId());
        assertEquals(Language.EN, nameEn.getId().getLanguage());
        assertEquals(Language.RU, nameRu.getId().getLanguage());
        assertEquals("Minsk", nameEn.getPlaceName());
        assertEquals("Минск", nameRu.getPlaceName());

        verify(locationApiClientProxy).fetch(TEST_LAT, TEST_LON, Language.EN);
        verify(locationApiClientProxy).fetch(TEST_LAT, TEST_LON, Language.RU);
        verifyNoMoreInteractions(locationApiClientProxy);
    }
}
