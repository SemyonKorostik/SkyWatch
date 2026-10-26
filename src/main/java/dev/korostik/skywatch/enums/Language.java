package dev.korostik.skywatch.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Language {
  RU, EN;

  Language() {
    if (name().length() != 2) {
      throw new IllegalStateException(
          "The Language constant name must contain 2 characters: " + name()
      );
    }
  }

  public static Optional<Language> from(String name) {
    return Arrays.stream(values()).filter(t -> t.name().equals(name.toUpperCase())).findFirst();
  }
}
