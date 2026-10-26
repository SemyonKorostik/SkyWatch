package dev.korostik.skywatch.telegram.handler.message;

import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.telegram.button.ButtonTypes;
import dev.korostik.skywatch.telegram.handler.message.buttons.ButtonHandler;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KeyboardButtonsUpdateHandler implements UpdateHandler {

  private final Map<ButtonTypes, ButtonHandler> buttonHandlers;

  @Autowired
  public KeyboardButtonsUpdateHandler(List<ButtonHandler> buttonHandlers) {
    this.buttonHandlers = buttonHandlers.stream()
        .collect(
            Collectors.toUnmodifiableMap(ButtonHandler::getType, buttonHandler -> buttonHandler));
  }

  @Override
  public void handle(Update update) {
    ButtonTypes.from(update.message().text())
        .map(buttonHandlers::get)
        .ifPresent(buttonHandler -> buttonHandler.handle(update));
  }
}
