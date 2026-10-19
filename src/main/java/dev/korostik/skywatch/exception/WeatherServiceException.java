package dev.korostik.skywatch.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class WeatherServiceException extends RuntimeException {

  private final List<WeatherProviderException> errors;

  public WeatherServiceException(String message, List<WeatherProviderException> errors) {
    super(message);
    this.errors = errors;
  }
}
