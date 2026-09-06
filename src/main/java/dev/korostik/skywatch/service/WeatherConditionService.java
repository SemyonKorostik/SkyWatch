package dev.korostik.skywatch.service;

import dev.korostik.skywatch.entity.WeatherCondition;
import dev.korostik.skywatch.repository.WeatherConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherConditionService {
    private final WeatherConditionRepository weatherConditionRepository;

    public WeatherCondition getByCode(Short code) {
        return weatherConditionRepository.findByCode(code).orElseThrow();
    }
}
