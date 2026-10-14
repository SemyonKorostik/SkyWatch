package dev.korostik.skywatch.config;

import dev.korostik.skywatch.exception.WeatherProviderClientException;
import dev.korostik.skywatch.exception.WeatherProviderException;
import dev.korostik.skywatch.exception.WeatherProviderRateLimitException;
import dev.korostik.skywatch.exception.WeatherProviderServerException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  @Bean
  public ErrorDecoder errorDecoder() {
    return (methodKey, response) -> {
      int status = response.status();

      if (status == 429) {
        return new WeatherProviderRateLimitException("Rate limited");
      }
      if (status >= 400 && status < 500) {
        return new WeatherProviderClientException("Client error: " + status);
      }
      if (status >= 500) {
        return new WeatherProviderServerException("Server error: " + status);
      }

      return new WeatherProviderException("Unexpected status: " + status);
    };
  }

  @Bean
  public Retryer retryer() {
    return new Retryer.Default(100, 1000, 3);
  }
}