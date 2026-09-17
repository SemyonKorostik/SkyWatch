package dev.korostik.skywatch.service;

import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.client.LocationApiClientProxy;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.User;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.mapper.LocationMapper;
import dev.korostik.skywatch.repository.LocationRepository;
import dev.korostik.skywatch.telegram.menu.KeyboardMenu;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

  @Value("${app.location.search-radius-meters}")
  private static Integer SEARCH_RADIUS_METERS;

  private final LocationApiClientProxy locationApiClientProxy;
  private final LocationMapper locationMapper;
  private final LocationRepository locationRepository;
  private final UserService userService;
  private final TelegramBotExecutor executor;
  private final KeyboardMenu keyboardMenu;


  public Location getNearestOrFetchAndSave(Double latitude, Double longitude, Language language) {
    return getNearest(latitude, longitude)
        .orElse(fetchAndSave(latitude, longitude, language)
            .orElseThrow());
  }

  public Optional<Location> fetchAndSave(Double latitude, Double longitude, Language language) {
    return locationApiClientProxy.getLocation(latitude, longitude, language)
        .map(locationMapper::mapToEntity)
        .map(locationRepository::save);
  }

  public Optional<Location> getNearest(Double latitude, Double longitude) {
    return locationRepository.findNearestByLatitudeAndLongitude(latitude, longitude,
        SEARCH_RADIUS_METERS);
  }

  @Transactional
  public void sendLocation(Long chatId, Double latitude, Double longitude, Language language) {
    User user = userService.getByChatId(chatId);
    user.setLocation(getNearestOrFetchAndSave(latitude, longitude, language));
    userService.save(user);
    SendMessage sendMessage = new SendMessage(chatId,
        "Your location is: " + user.getLocation());
    sendMessage.setReplyMarkup(keyboardMenu.getForecastReplyMarkup());
    executor.execute(sendMessage);
  }
}
