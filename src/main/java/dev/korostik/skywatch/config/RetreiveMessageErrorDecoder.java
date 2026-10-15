package dev.korostik.skywatch.config;

import dev.korostik.skywatch.exception.WeatherProviderException;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.http.HttpStatus;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class RetreiveMessageErrorDecoder implements ErrorDecoder {

  private ErrorDecoder errorDecoder = new Default();

  @Override
  public Exception decode(String methodKey, Response response) {
    HttpStatus status = HttpStatus.valueOf(response.status());
    String body = "";
    Map<String, Object> parsedBody = null;

    // 1. Пытаемся прочитать тело ОДИН РАЗ (InputStream не перечитать)
    try (InputStream is = response.body().asInputStream()) {
      body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      // Пытаемся распарсить в общую Map для логов (если это JSON)
      if (!body.isBlank() && body.trim().startsWith("{")) {
        ObjectMapper objectMapper = new ObjectMapper();
        parsedBody = objectMapper.readValue(body, new TypeReference<>() {
        });
      }
    } catch (IOException ignored) { /* тело не JSON или пустое */ }

    // 2. Формируем наше единое исключение
    WeatherProviderException ex = new WeatherProviderException(
        "Provider call failed: %s %s -> %d %s".formatted(
            response.request().httpMethod(),
            response.request().url(),
            response.status(),
            body // кладем сырое тело для логов
        ),
        status,
        response.headers()
    );

    // 3. (Опционально) Если нужно достать код ошибки провайдера для специфической логики
    // ex.setProviderErrorCode(parsedBody != null ? (String) parsedBody.get("code") : null);

    return ex;
  }

  ;
}