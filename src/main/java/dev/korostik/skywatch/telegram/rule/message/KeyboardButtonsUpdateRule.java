package dev.korostik.skywatch.telegram.rule.message;

import com.pengrad.telegrambot.model.Message;
import dev.korostik.skywatch.telegram.button.ButtonTypes;
import dev.korostik.skywatch.telegram.handler.message.KeyboardButtonsUpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.matcher.Matcher;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeyboardButtonsUpdateRule implements MessageUpdateRule {

  private final KeyboardButtonsUpdateHandler keyboardButtonsUpdateHandler;

  private static final Set<String> COMMANDS = Arrays.stream(ButtonTypes.values())
      .map(ButtonTypes::getName)
      .collect(Collectors.toSet());

  @Override
  public Matcher<Message> matcher() {
    return m -> COMMANDS.contains(m.text());
  }

  @Override
  public UpdateHandler handler() {
    return keyboardButtonsUpdateHandler;
  }
}
