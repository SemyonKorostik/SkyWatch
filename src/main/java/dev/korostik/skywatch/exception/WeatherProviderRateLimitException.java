package dev.korostik.skywatch.exception;

public class WeatherProviderRateLimitException extends RuntimeException {

  public WeatherProviderRateLimitException(String message) {
    super(message);
  }
}
