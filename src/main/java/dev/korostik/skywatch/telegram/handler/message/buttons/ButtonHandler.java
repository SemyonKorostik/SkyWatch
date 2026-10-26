package dev.korostik.skywatch.telegram.handler.message.buttons;

import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.telegram.button.ButtonTypes;

public interface ButtonHandler {

  void handle(Update update);

  ButtonTypes getType();
}
