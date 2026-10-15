package dev.korostik.skywatch.config;

import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  @Bean
  public ErrorDecoder errorDecoder() {
    return new RetreiveMessageErrorDecoder();
  }

  @Bean
  public Retryer retryer() {
    return new Retryer.Default(100, 1000, 3);
  }
}