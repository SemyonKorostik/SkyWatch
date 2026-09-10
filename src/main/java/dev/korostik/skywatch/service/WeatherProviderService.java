package dev.korostik.skywatch.service;

import dev.korostik.skywatch.entity.WeatherProvider;
import dev.korostik.skywatch.repository.WeatherProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherProviderService {
    private final WeatherProviderRepository weatherProviderRepository;

    public WeatherProvider getByName(String name) {
        return weatherProviderRepository.findByName(name).orElseThrow();
    }
}
