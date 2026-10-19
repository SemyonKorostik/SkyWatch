package dev.korostik.skywatch.exception;

import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WeatherProviderException extends RuntimeException {

    private final HttpStatus status;
    private final Map<String, Collection<String>> headers;
    private final String providerName;

    public WeatherProviderException(String message, HttpStatus status, Map<String, Collection<String>> headers, String providerName) {
        super(message);
        this.status = status;
        this.headers = headers;
        this.providerName = providerName;
    }

    public boolean isRetryable() {
        return status.is5xxServerError() || status == HttpStatus.TOO_MANY_REQUESTS;
    }

}
