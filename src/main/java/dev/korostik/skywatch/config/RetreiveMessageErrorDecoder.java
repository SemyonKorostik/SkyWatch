package dev.korostik.skywatch.config;

import dev.korostik.skywatch.exception.WeatherProviderException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetreiveMessageErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        String body = "";
        Map<String, Object> parsedBody = null;

        try (InputStream is = response.body().asInputStream()) {
            body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            if (!body.isBlank() && body.trim().startsWith("{")) {
                parsedBody = objectMapper.readValue(body, Map.class);
            }
        } catch (IOException ignored) {
        }

        String providerName = methodKey.split("#")[0];
        String errorMessage = "Provider '%s' failed: %d %s".formatted(providerName, status.value(), body);

        log.warn("Hey, Feign error: {}", errorMessage);

        return new WeatherProviderException(errorMessage, status, response.headers(), providerName);
    }
}
