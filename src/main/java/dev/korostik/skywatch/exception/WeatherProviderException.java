package dev.korostik.skywatch.exception;


import java.util.Collection;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class WeatherProviderException extends RuntimeException {

  private final HttpStatus status;
  private final Map<String, Collection<String>> headers;

  public WeatherProviderException(String message, HttpStatus status,
      Map<String, Collection<String>> headers) {
    super(message);
    this.status = status;
    this.headers = headers;
  }

  public boolean isRetryable() {
    // 5xx или 429 (Too Many Requests) — стоит ретраить/переключать провайдера
    return status.is5xxServerError() || status == HttpStatus.TOO_MANY_REQUESTS;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
