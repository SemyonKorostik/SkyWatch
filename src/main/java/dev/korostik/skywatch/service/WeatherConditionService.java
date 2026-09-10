package dev.korostik.skywatch.service;

import dev.korostik.skywatch.entity.WeatherCondition;
import dev.korostik.skywatch.entity.WeatherProvider;
import dev.korostik.skywatch.repository.WeatherConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherConditionService {
    private final WeatherProviderService weatherProviderService;
    private final WeatherConditionRepository weatherConditionRepository;

    public WeatherCondition getByCode(String providerName, String providerCode) {
        WeatherProvider provider = weatherProviderService.getByName(providerName);
        return weatherConditionRepository.findByProviderNameAndProviderCode(provider, providerCode).orElseThrow();
    }
}
