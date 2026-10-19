package dev.korostik.skywatch.exception;

public class WeatherProviderNotFoundException extends RuntimeException {

  public WeatherProviderNotFoundException(String message) {
    super("Weather provider not found: " + message);
  }
}
