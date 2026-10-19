package dev.korostik.skywatch.exception.handler;

import dev.korostik.skywatch.exception.WeatherServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WeatherExceptionHandler {

  @ExceptionHandler(WeatherServiceException.class)
  public void handle(WeatherServiceException e) {

  }
}
