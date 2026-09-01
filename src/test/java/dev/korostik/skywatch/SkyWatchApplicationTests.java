package dev.korostik.skywatch;

import dev.korostik.skywatch.service.LocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SkyWatchApplicationTests {

    @Autowired(required = false)
    LocationService locationService;

//    @Autowired(required = false)
//    LocationRepository locationRepository;

//    @Autowired(required = false)
//    TelegramBotApiClientProxy telegramBotApiClientProxy;

    @Test
    void contextLoads() {
//        GeonamesResponse location = locationService.getLocation("54.224", "28.511", Language.RU);
//        System.out.println(location);

//        List<UpdateDto> updates = telegramBotApiClientProxy.getUpdates(0, 10);
//        System.out.println(updates);
//        Assertions.assertNotNull(updates);
        Assertions.assertNotNull(locationService);
//        Assertions.assertNotNull(locationRepository);
//        Assertions.assertNotNull(telegramBotApiClientProxy);
    }

}
