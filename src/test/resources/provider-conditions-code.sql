INSERT INTO wmo_weather_codes (code, description, category, subcategory, emoji, intensity,
                               weather_group, created_at, updated_at)
VALUES ('00', 'Cloud development not observed or not observable', 'No precipitation', 'Cloud cover',
        '☀️', NULL, 'clear', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO weather_provider (name, api_url, priority, is_enabled)
VALUES ('open-meteo', 'https://api.open-meteo.com/v1', 1, true);

INSERT INTO weather_conditions (id, provider_name, provider_code, wmo_code)
VALUES (1, 'open-meteo', '0', '00');
