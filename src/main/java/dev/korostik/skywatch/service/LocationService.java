package dev.korostik.skywatch.service;

import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.client.LocationApiClientProxy;
import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.User;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.mapper.LocationMapper;
import dev.korostik.skywatch.repository.LocationRepository;
import dev.korostik.skywatch.telegram.menu.KeyboardMenu;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

  @Value("${app.location.search-radius-meters}")
  private Integer SEARCH_RADIUS_METERS;

  private final LocationApiClientProxy locationApiClientProxy;
  private final LocationMapper locationMapper;
  private final LocationRepository locationRepository;
  private final TelegramBotExecutor executor;
  private final KeyboardMenu keyboardMenu;

  public Optional<Location> fetch(Double latitude, Double longitude, Language language) {
    return locationApiClientProxy.getLocation(latitude, longitude, language)
        .map(locationMapper::mapToEntity);
  }

  public Optional<Location> getNearest(Double latitude, Double longitude) {
    return locationRepository.findNearestByLatitudeAndLongitude(latitude, longitude,
        SEARCH_RADIUS_METERS);
  }

  @Transactional
  public Location save(Location location) {
    return locationRepository.save(location);
  }

  public void sendLocation(Long chatId, Location location) {
    SendMessage sendMessage = new SendMessage(chatId, "Your location is: " + location);
    sendMessage.setReplyMarkup(keyboardMenu.getForecastReplyMarkup());
    executor.execute(sendMessage);
  }
}
