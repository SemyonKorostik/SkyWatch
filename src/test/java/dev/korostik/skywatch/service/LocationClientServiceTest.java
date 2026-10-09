package dev.korostik.skywatch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import dev.korostik.skywatch.client.LocationApiClientProxy;
import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.entities.LocationNameId;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.repository.LocationNameRepository;
import dev.korostik.skywatch.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(dev.korostik.skywatch.config.TestContainerConfiguration.class)
class LocationClientServiceTest {

  /*  @Autowired
    private LocationApiClientProxy locationApiClientProxy;

    @Autowired
    private LocationClientService locationClientService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationNameRepository locationNameRepository;

    private static final Double TEST_LAT = 53.9045;
    private static final Double TEST_LON = 27.5615;
    private static final Language TEST_LANG = Language.EN;
    private static final String TEST_PLACE_NAME = "Minsk";
    private static final String TEST_COUNTRY = "Belarus";
    private static final int TEST_TIMEZONE_OFFSET = 3;
    private static final String TEST_TIMEZONE_ID = "Europe/Minsk";

    private GeoNameDto createTestGeoNameDto() {
        return new GeoNameDto(
                TEST_PLACE_NAME,
                TEST_LAT,
                TEST_LON,
                TEST_COUNTRY,
                "P",
                "PPL",
                new TimeZoneDto(TEST_TIMEZONE_OFFSET, TEST_TIMEZONE_ID, TEST_TIMEZONE_OFFSET)
        );
    }

    @Test
    void findOrCreateLocationName_locationAndNameExist_returnsExisting() {
        // Arrange: Save existing location and name
        Location existingLocation = new Location();
        existingLocation.setLatitude(TEST_LAT);
        existingLocation.setLongitude(TEST_LON);
        existingLocation.setTimeZoneId(java.time.ZoneId.of(TEST_TIMEZONE_ID));
        existingLocation = locationRepository.save(existingLocation);

        LocationName existingName = new LocationName();
        existingName.setId(new LocationNameId(existingLocation.getId(), TEST_LANG));
        existingName.setLocation(existingLocation);
        existingName.setPlaceName(TEST_PLACE_NAME);
        existingName.setCountry(TEST_COUNTRY);
        locationNameRepository.save(existingName);

        // Mock API (won't be called if location and name exist)
        when(locationApiClientProxy.fetch(anyDouble(), anyDouble(), any()))
                .thenThrow(new AssertionError("API should not be called"));

        // Act
        LocationName result = locationClientService.findOrCreateLocationName(TEST_LAT, TEST_LON, TEST_LANG);

        // Assert
        assertNotNull(result);
        assertEquals(existingName.getId(), result.getId());
        assertEquals(existingName.getPlaceName(), result.getPlaceName());
        assertEquals(existingName.getCountry(), result.getCountry());
        assertSame(existingName.getLocation(), result.getLocation());

        // Verify API was not called
        verify(locationApiClientProxy, never()).fetch(anyDouble(), anyDouble(), any());
    }

    @Test
    void findOrCreateLocationName_locationExists_nameMissing_createsNameOnly() {
        // Arrange: Save location without name for TEST_LANG
        Location existingLocation = new Location();
        existingLocation.setLatitude(TEST_LAT);
        existingLocation.setLongitude(TEST_LON);
        existingLocation.setTimeZoneId(java.time.ZoneId.of(TEST_TIMEZONE_ID));
        existingLocation = locationRepository.save(existingLocation);

        // Mock API to return our test DTO
        GeoNameDto dto = createTestGeoNameDto();
        when(locationApiClientProxy.fetch(TEST_LAT, TEST_LON, TEST_LANG))
                .thenReturn(java.util.Optional.of(dto));

        // Act
        LocationName result = locationClientService.findOrCreateLocationName(TEST_LAT, TEST_LON, TEST_LANG);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(existingLocation.getId(), result.getId().getLocationId());
        assertEquals(TEST_LANG.name(), result.getId().getLanguage());
        assertEquals(dto.name(), result.getPlaceName());
        assertEquals(dto.countryName(), result.getCountry());
        assertSame(existingLocation, result.getLocation());

        // Verify API was called once
        verify(locationApiClientProxy).fetch(TEST_LAT, TEST_LON, TEST_LANG);
        verifyNoMoreInteractions(locationApiClientProxy);

        // Verify no new location was created
        assertEquals(1L, locationRepository.count());
    }

    @Test
    void findOrCreateLocationName_locationMissing_createsLocationAndName() {
        // Arrange: No existing location
        assertEquals(0L, locationRepository.count());

        // Mock API to return our test DTO
        GeoNameDto dto = createTestGeoNameDto();
        when(locationApiClientProxy.fetch(TEST_LAT, TEST_LON, TEST_LANG))
                .thenReturn(java.util.Optional.of(dto));

        // Act
        LocationName result = locationClientService.findOrCreateLocationName(TEST_LAT, TEST_LON, TEST_LANG);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getId().getLocationId());
        assertEquals(TEST_LANG.name(), result.getId().getLanguage());
        assertEquals(dto.name(), result.getPlaceName());
        assertEquals(dto.countryName(), result.getCountry());
        assertNotNull(result.getLocation());
        assertEquals(dto.latitude(), result.getLocation().getLatitude());
        assertEquals(dto.longitude(), result.getLocation().getLongitude());
        assertEquals(java.time.ZoneId.of(dto.timeZone().timeZoneId()), result.getLocation().getTimeZoneId());

        // Verify API was called once
        verify(locationApiClientProxy).fetch(TEST_LAT, TEST_LON, TEST_LANG);
        verifyNoMoreInteractions(locationApiClientProxy);

        // Verify exactly one location and one name were created
        assertEquals(1L, locationRepository.count());
        assertEquals(1L, locationNameRepository.count());
    }

    @Test
    void findOrCreateLocationName_apiReturnsEmpty_throwsException() {
        // Arrange: Mock API to return empty
        when(locationApiClientProxy.fetch(anyDouble(), anyDouble(), any()))
                .thenReturn(java.util.Optional.empty());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> locationClientService.findOrCreateLocationName(TEST_LAT, TEST_LON, TEST_LANG));

        assertTrue(exception.getMessage().contains("Hey there!"));

        // Verify API was called once
        verify(locationApiClientProxy).fetch(TEST_LAT, TEST_LON, TEST_LANG);
        verifyNoMoreInteractions(locationApiClientProxy);

        // Verify nothing was saved to DB
        assertEquals(0L, locationRepository.count());
        assertEquals(0L, locationNameRepository.count());
    }

    @Test
    void findOrCreateLocationName_differentLanguage_sameLocation_createsNewName() {
        // Arrange: Save location and name in EN
        Location location = new Location();
        location.setLatitude(TEST_LAT);
        location.setLongitude(TEST_LON);
        location.setTimeZoneId(java.time.ZoneId.of(TEST_TIMEZONE_ID));
        location = locationRepository.save(location);

        LocationName existingEnName = new LocationName();
        existingEnName.setId(new LocationNameId(location.getId(), Language.EN));
        existingEnName.setLocation(location);
        existingEnName.setPlaceName(TEST_PLACE_NAME);
        existingEnName.setCountry(TEST_COUNTRY);
        locationNameRepository.save(existingEnName);

        // Mock API to return DTO for RU (different place name)
        GeoNameDto ruDto = new GeoNameDto(
                "Минск", // Minsk in Russian
                TEST_LAT,
                TEST_LON,
                "Беларусь", // Belarus in Russian
                "P",
                "PPL",
                new TimeZoneDto(TEST_TIMEZONE_OFFSET, TEST_TIMEZONE_ID, TEST_TIMEZONE_OFFSET)
        );
        when(locationApiClientProxy.fetch(TEST_LAT, TEST_LON, Language.RU))
                .thenReturn(java.util.Optional.of(ruDto));

        // Act: Request location name in RU
        LocationName result = locationClientService.findOrCreateLocationName(TEST_LAT, TEST_LON, Language.RU);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(location.getId(), result.getId().getLocationId());
        assertEquals(Language.RU.name(), result.getId().getLanguage());
        assertEquals(ruDto.name(), result.getPlaceName());
        assertEquals(ruDto.countryName(), result.getCountry());
        assertSame(location, result.getLocation());

        // Verify API was called once for RU
        verify(locationApiClientProxy).fetch(TEST_LAT, TEST_LON, Language.RU);
        verifyNoMoreInteractions(locationApiClientProxy);

        // Verify we still have only one location, but two names
        assertEquals(1L, locationRepository.count());
        assertEquals(2L, locationNameRepository.count());
    }*/
}
