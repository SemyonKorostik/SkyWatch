package dev.korostik.skywatch.config;

import dev.korostik.skywatch.exception.WeatherProviderException;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetreiveMessageErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {
    HttpStatus status = HttpStatus.valueOf(response.status());
    String body = "";

    try (InputStream is = response.body().asInputStream()) {
      body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException ignored) {
    }

    String providerName = methodKey.split("#")[0];
    String errorMessage = "Provider '%s' failed: %d %s".formatted(providerName, status.value(),
        body);

    log.warn("Feign error: {}", errorMessage);

    return new WeatherProviderException(errorMessage, status, response.headers(), providerName);
  }
}
