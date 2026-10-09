package dev.korostik.skywatch.enums;

public enum Language {
  RU, EN;

  Language() {
    if (name().length() != 2) {
      throw new IllegalStateException(
          "The Language constant name must contain 2 characters: " + name()
      );
    }
  }
}
