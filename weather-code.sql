-- 1. Создаем основную таблицу с кодами WMO
CREATE TABLE IF NOT EXISTS wmo_weather_codes (
                                                 id SERIAL PRIMARY KEY,
                                                 code VARCHAR(2) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    subcategory VARCHAR(100),
    emoji VARCHAR(10) NOT NULL,
    intensity VARCHAR(20),
    weather_group VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 2. Создаем индексы для быстрого поиска
CREATE INDEX IF NOT EXISTS idx_wmo_code ON wmo_weather_codes(code);
-- CREATE INDEX IF NOT EXISTS idx_wmo_group ON wmo_weather_codes(weather_group);
-- CREATE INDEX IF NOT EXISTS idx_wmo_category ON wmo_weather_codes(category);

-- 3. Вставляем данные с группировкой
INSERT INTO wmo_weather_codes (code, description, category, subcategory, emoji, intensity, weather_group) VALUES

-- ГРУППА 1: Без осадков, тумана и т.д. (00-19)
('00', 'Cloud development not observed or not observable', 'No precipitation', 'Cloud cover', '☀️', NULL, 'clear'),
('01', 'Clouds generally dissolving or becoming less developed', 'No precipitation', 'Cloud cover', '⛅', NULL, 'cloudy'),
('02', 'State of sky on the whole unchanged', 'No precipitation', 'Cloud cover', '☁️', NULL, 'cloudy'),
('03', 'Clouds generally forming or developing', 'No precipitation', 'Cloud cover', '☁️', NULL, 'cloudy'),
('04', 'Visibility reduced by smoke (e.g. veldt or forest fires, industrial smoke or volcanic ashes)', 'No precipitation', 'Smoke', '🌫️', NULL, 'fog'),
('05', 'Haze', 'No precipitation', 'Haze', '🌫️', NULL, 'fog'),
('06', 'Widespread dust in suspension in the air', 'No precipitation', 'Dust', '💨', NULL, 'fog'),
('07', 'Dust or sand raised by wind', 'No precipitation', 'Dust', '💨', NULL, 'fog'),
('08', 'Well developed dust whirl(s) or sand whirl(s)', 'No precipitation', 'Dust', '🌪️', NULL, 'storm'),
('09', 'Duststorm or sandstorm within sight', 'No precipitation', 'Dust', '🌪️', NULL, 'storm'),
('10', 'Mist', 'No precipitation', 'Mist', '🌫️', NULL, 'fog'),
('11', 'Patches of shallow fog or ice fog', 'No precipitation', 'Fog', '🌫️', NULL, 'fog'),
('12', 'More or less continuous fog or ice fog', 'No precipitation', 'Fog', '🌫️', NULL, 'fog'),
('13', 'Lightning visible, no thunder heard', 'No precipitation', 'Thunderstorm', '⚡', NULL, 'thunder'),
('14', 'Precipitation within sight, not reaching the ground or the surface of the sea', 'No precipitation', 'Precipitation', '🌧️', NULL, 'rain'),
('15', 'Precipitation within sight, reaching the ground, but distant (>5 km)', 'No precipitation', 'Precipitation', '🌧️', NULL, 'rain'),
('16', 'Precipitation within sight, reaching the ground, near to station', 'No precipitation', 'Precipitation', '🌧️', NULL, 'rain'),
('17', 'Thunderstorm, but no precipitation at the time of observation', 'No precipitation', 'Thunderstorm', '⛈️', NULL, 'thunder'),
('18', 'Squalls', 'No precipitation', 'Squalls', '💨', NULL, 'storm'),
('19', 'Funnel cloud(s) (tornado cloud or water-spout)', 'No precipitation', 'Tornado', '🌪️', NULL, 'tornado'),

-- ГРУППА 2: Осадки, туман или гроза в течение часа (20-29)
('20', 'Drizzle (not freezing) or snow grains', 'Precipitation (past hour)', 'Drizzle', '🌦️', 'slight', 'drizzle'),
('21', 'Rain (not freezing)', 'Precipitation (past hour)', 'Rain', '🌧️', NULL, 'rain'),
('22', 'Snow', 'Precipitation (past hour)', 'Snow', '❄️', NULL, 'snow'),
('23', 'Rain and snow or ice pellets', 'Precipitation (past hour)', 'Mixed', '🌨️', NULL, 'snow'),
('24', 'Freezing drizzle or freezing rain', 'Precipitation (past hour)', 'Freezing rain', '🌧️', NULL, 'rain'),
('25', 'Shower(s) of rain', 'Precipitation (past hour)', 'Rain showers', '🌦️', NULL, 'rain'),
('26', 'Shower(s) of snow, or of rain and snow', 'Precipitation (past hour)', 'Snow showers', '🌨️', NULL, 'snow'),
('27', 'Shower(s) of hail, or of rain and hail', 'Precipitation (past hour)', 'Hail', '🌧️', NULL, 'rain'),
('28', 'Fog or ice fog', 'Precipitation (past hour)', 'Fog', '🌫️', NULL, 'fog'),
('29', 'Thunderstorm (with or without precipitation)', 'Precipitation (past hour)', 'Thunderstorm', '⛈️', NULL, 'thunder'),

-- ГРУППА 3: Пыльные бури, песчаные бури, поземок (30-39)
('30', 'Slight or moderate duststorm/sandstorm - decreased during preceding hour', 'Duststorm', 'Duststorm', '🌪️', 'slight', 'storm'),
('31', 'Slight or moderate duststorm/sandstorm - no appreciable change', 'Duststorm', 'Duststorm', '🌪️', 'slight', 'storm'),
('32', 'Slight or moderate duststorm/sandstorm - begun or increased', 'Duststorm', 'Duststorm', '🌪️', 'slight', 'storm'),
('33', 'Severe duststorm/sandstorm - decreased during preceding hour', 'Duststorm', 'Duststorm', '🌪️', 'severe', 'storm'),
('34', 'Severe duststorm/sandstorm - no appreciable change', 'Duststorm', 'Duststorm', '🌪️', 'severe', 'storm'),
('35', 'Severe duststorm/sandstorm - begun or increased', 'Duststorm', 'Duststorm', '🌪️', 'severe', 'storm'),
('36', 'Slight or moderate blowing snow - generally low (below eye level)', 'Blowing snow', 'Drifting snow', '🌨️', 'slight', 'snow'),
('37', 'Heavy drifting snow', 'Blowing snow', 'Drifting snow', '🌨️', 'heavy', 'snow'),
('38', 'Slight or moderate blowing snow - generally high (above eye level)', 'Blowing snow', 'Drifting snow', '🌨️', 'slight', 'snow'),
('39', 'Heavy drifting snow', 'Blowing snow', 'Drifting snow', '🌨️', 'heavy', 'snow'),

-- ГРУППА 4: Туман или ледяной туман в момент наблюдения (40-49)
('40', 'Fog or ice fog at a distance, not at station during preceding hour', 'Fog', 'Fog', '🌫️', NULL, 'fog'),
('41', 'Fog or ice fog in patches', 'Fog', 'Fog', '🌫️', NULL, 'fog'),
('42', 'Fog or ice fog, sky visible - became thinner during preceding hour', 'Fog', 'Fog', '🌫️', NULL, 'fog'),
('43', 'Fog or ice fog, sky invisible', 'Fog', 'Fog', '🌫️', NULL, 'fog'),
('44', 'Fog or ice fog, sky visible - no appreciable change', 'Fog', 'Fog', '🌫️', NULL, 'fog'),
('45', 'Fog or ice fog, sky invisible', 'Fog', 'Fog', '🌫️', NULL, 'fog'),
('46', 'Fog or ice fog, sky visible - began or became thicker', 'Fog', 'Fog', '🌫️', NULL, 'fog'),
('47', 'Fog or ice fog, sky invisible', 'Fog', 'Fog', '🌫️', NULL, 'fog'),
('48', 'Fog, depositing rime, sky visible', 'Fog', 'Fog with rime', '🌫️', NULL, 'fog'),
('49', 'Fog, depositing rime, sky invisible', 'Fog', 'Fog with rime', '🌫️', NULL, 'fog'),

-- ГРУППА 5: Морось (50-59)
('50', 'Drizzle, not freezing, intermittent - slight', 'Drizzle', 'Drizzle', '🌦️', 'slight', 'drizzle'),
('51', 'Drizzle, not freezing, continuous - slight', 'Drizzle', 'Drizzle', '🌦️', 'slight', 'drizzle'),
('52', 'Drizzle, not freezing, intermittent - moderate', 'Drizzle', 'Drizzle', '🌧️', 'moderate', 'drizzle'),
('53', 'Drizzle, not freezing, continuous - moderate', 'Drizzle', 'Drizzle', '🌧️', 'moderate', 'drizzle'),
('54', 'Drizzle, not freezing, intermittent - heavy (dense)', 'Drizzle', 'Drizzle', '🌧️', 'heavy', 'drizzle'),
('55', 'Drizzle, not freezing, continuous - heavy (dense)', 'Drizzle', 'Drizzle', '🌧️', 'heavy', 'drizzle'),
('56', 'Drizzle, freezing - slight', 'Drizzle', 'Freezing drizzle', '🌧️', 'slight', 'drizzle'),
('57', 'Drizzle, freezing - moderate or heavy', 'Drizzle', 'Freezing drizzle', '🌧️', 'moderate', 'drizzle'),
('58', 'Drizzle and rain - slight', 'Drizzle', 'Drizzle and rain', '🌧️', 'slight', 'drizzle'),
('59', 'Drizzle and rain - moderate or heavy', 'Drizzle', 'Drizzle and rain', '🌧️', 'moderate', 'drizzle'),

-- ГРУППА 6: Дождь (60-69)
('60', 'Rain, not freezing, intermittent - slight', 'Rain', 'Rain', '🌦️', 'slight', 'rain'),
('61', 'Rain, not freezing, continuous - slight', 'Rain', 'Rain', '🌦️', 'slight', 'rain'),
('62', 'Rain, not freezing, intermittent - moderate', 'Rain', 'Rain', '🌧️', 'moderate', 'rain'),
('63', 'Rain, not freezing, continuous - moderate', 'Rain', 'Rain', '🌧️', 'moderate', 'rain'),
('64', 'Rain, not freezing, intermittent - heavy', 'Rain', 'Rain', '🌧️', 'heavy', 'rain'),
('65', 'Rain, not freezing, continuous - heavy', 'Rain', 'Rain', '🌧️', 'heavy', 'rain'),
('66', 'Rain, freezing - slight', 'Rain', 'Freezing rain', '🌧️', 'slight', 'rain'),
('67', 'Rain, freezing - moderate or heavy', 'Rain', 'Freezing rain', '🌧️', 'moderate', 'rain'),
('68', 'Rain or drizzle and snow - slight', 'Rain', 'Mixed', '🌨️', 'slight', 'rain'),
('69', 'Rain or drizzle and snow - moderate or heavy', 'Rain', 'Mixed', '🌨️', 'moderate', 'rain'),

-- ГРУППА 7: Твердые осадки (70-79)
('70', 'Intermittent fall of snowflakes - slight', 'Snow', 'Snow', '❄️', 'slight', 'snow'),
('71', 'Continuous fall of snowflakes - slight', 'Snow', 'Snow', '❄️', 'slight', 'snow'),
('72', 'Intermittent fall of snowflakes - moderate', 'Snow', 'Snow', '❄️', 'moderate', 'snow'),
('73', 'Continuous fall of snowflakes - moderate', 'Snow', 'Snow', '❄️', 'moderate', 'snow'),
('74', 'Intermittent fall of snowflakes - heavy', 'Snow', 'Snow', '❄️', 'heavy', 'snow'),
('75', 'Continuous fall of snowflakes - heavy', 'Snow', 'Snow', '❄️', 'heavy', 'snow'),
('76', 'Diamond dust (with or without fog)', 'Snow', 'Diamond dust', '❄️', NULL, 'snow'),
('77', 'Snow grains (with or without fog)', 'Snow', 'Snow grains', '❄️', NULL, 'snow'),
('78', 'Isolated star-like snow crystals (with or without fog)', 'Snow', 'Snow crystals', '❄️', NULL, 'snow'),
('79', 'Ice pellets', 'Snow', 'Ice pellets', '🌨️', NULL, 'snow'),

-- ГРУППА 8: Ливневые осадки (80-90)
('80', 'Rain shower(s) - slight', 'Showers', 'Rain showers', '🌦️', 'slight', 'rain'),
('81', 'Rain shower(s) - moderate or heavy', 'Showers', 'Rain showers', '🌧️', 'moderate', 'rain'),
('82', 'Rain shower(s) - violent', 'Showers', 'Rain showers', '⛈️', 'violent', 'rain'),
('83', 'Shower(s) of rain and snow mixed - slight', 'Showers', 'Mixed showers', '🌨️', 'slight', 'snow'),
('84', 'Shower(s) of rain and snow mixed - moderate or heavy', 'Showers', 'Mixed showers', '🌨️', 'moderate', 'snow'),
('85', 'Snow shower(s) - slight', 'Showers', 'Snow showers', '❄️', 'slight', 'snow'),
('86', 'Snow shower(s) - moderate or heavy', 'Showers', 'Snow showers', '❄️', 'moderate', 'snow'),
('87', 'Shower(s) of snow pellets or small hail - slight', 'Showers', 'Hail showers', '🌨️', 'slight', 'snow'),
('88', 'Shower(s) of snow pellets or small hail - moderate or heavy', 'Showers', 'Hail showers', '🌨️', 'moderate', 'snow'),
('89', 'Shower(s) of hail, without thunder - slight', 'Showers', 'Hail showers', '🌧️', 'slight', 'rain'),
('90', 'Shower(s) of hail, without thunder - moderate or heavy', 'Showers', 'Hail showers', '🌧️', 'moderate', 'rain'),

-- ГРУППА 9: Осадки с грозой (91-99)
('91', 'Slight rain at time of observation - thunderstorm during preceding hour', 'Thunderstorm', 'Rain with thunder', '⛈️', 'slight', 'thunder'),
('92', 'Moderate or heavy rain at time of observation - thunderstorm during preceding hour', 'Thunderstorm', 'Rain with thunder', '⛈️', 'moderate', 'thunder'),
('93', 'Slight snow, or rain and snow mixed or hail at time of observation - thunderstorm during preceding hour', 'Thunderstorm', 'Snow with thunder', '⛈️', 'slight', 'thunder'),
('94', 'Moderate or heavy snow, or rain and snow mixed or hail - thunderstorm during preceding hour', 'Thunderstorm', 'Snow with thunder', '⛈️', 'moderate', 'thunder'),
('95', 'Thunderstorm, slight or moderate, without hail - with rain and/or snow', 'Thunderstorm', 'Thunderstorm', '⛈️', 'moderate', 'thunder'),
('96', 'Thunderstorm, slight or moderate, with hail', 'Thunderstorm', 'Thunderstorm with hail', '⛈️', 'moderate', 'thunder'),
('97', 'Thunderstorm, heavy, without hail - with rain and/or snow', 'Thunderstorm', 'Thunderstorm', '⛈️', 'heavy', 'thunder'),
('98', 'Thunderstorm combined with duststorm or sandstorm', 'Thunderstorm', 'Duststorm with thunder', '⛈️', 'severe', 'thunder'),
('99', 'Thunderstorm, heavy, with hail', 'Thunderstorm', 'Thunderstorm with hail', '⛈️', 'heavy', 'thunder');

-- -- 4. Создаем представление для удобного доступа
-- CREATE OR REPLACE VIEW vw_weather_codes AS
-- SELECT
--     code,
--     description,
--     category,
--     subcategory,
--     emoji,
--     intensity,
--     weather_group,
--     CASE
--         WHEN intensity IS NULL THEN emoji
--         ELSE CONCAT(emoji, ' (', intensity, ')')
--         END as display_text
-- FROM wmo_weather_codes
-- ORDER BY code::integer;
--
-- -- 5. Создаем функцию для быстрого получения эмодзи по коду
-- CREATE OR REPLACE FUNCTION get_weather_emoji(p_code VARCHAR)
-- RETURNS VARCHAR
-- LANGUAGE plpgsql
-- AS $$
-- DECLARE
-- v_emoji VARCHAR(10);
-- BEGIN
-- SELECT emoji INTO v_emoji
-- FROM wmo_weather_codes
-- WHERE code = LPAD(p_code, 2, '0');
--
-- RETURN COALESCE(v_emoji, '🌤️');
-- END;
-- $$;
--
-- -- 6. Создаем функцию для получения погоды по коду
-- CREATE OR REPLACE FUNCTION get_weather_info(p_code VARCHAR)
-- RETURNS TABLE(
--     weather_code VARCHAR,
--     description TEXT,
--     emoji VARCHAR,
--     weather_group VARCHAR,
--     intensity VARCHAR
-- )
-- LANGUAGE plpgsql
-- AS $$
-- BEGIN
-- RETURN QUERY
-- SELECT
--     wc.code,
--     wc.description,
--     wc.emoji,
--     wc.weather_group,
--     wc.intensity
-- FROM wmo_weather_codes wc
-- WHERE wc.code = LPAD(p_code, 2, '0');
-- END;
-- $$;
--
-- -- 7. Создаем агрегированную статистику по группам
-- CREATE OR REPLACE VIEW vw_weather_group_stats AS
-- SELECT
--     weather_group,
--     COUNT(*) as total_codes,
--     STRING_AGG(DISTINCT category, ', ') as categories,
--     STRING_AGG(DISTINCT subcategory, ', ') as subcategories
-- FROM wmo_weather_codes
-- GROUP BY weather_group
-- ORDER BY weather_group;

INSERT INTO weather_provider (name, api_url, priority, is_enabled)
VALUES ('open-meteo', 'https://api.open-meteo.com/v1', 1, true)
ON CONFLICT (name) DO NOTHING;

-- 2. Сопоставляем коды Open-Meteo с WMO-кодами
INSERT INTO weather_conditions (provider_name, provider_code, wmo_code)
VALUES
    -- Ясно и облачно (Clear / Cloudy)
    ('open-meteo', '0', '00'),   -- Clear sky -> Cloud development not observed
    ('open-meteo', '1', '01'),   -- Mainly clear -> Clouds generally dissolving
    ('open-meteo', '2', '02'),   -- Partly cloudy -> State of sky on the whole unchanged
    ('open-meteo', '3', '03'),   -- Overcast -> Clouds generally forming

    -- Туман (Fog)
    ('open-meteo', '45', '40'),  -- Fog -> Fog or ice fog at a distance
    ('open-meteo', '48', '48'),  -- Depositing rime fog -> Fog, depositing rime, sky visible

    -- Морось (Drizzle)
    ('open-meteo', '51', '51'),  -- Light drizzle -> Drizzle, not freezing, continuous - slight
    ('open-meteo', '53', '52'),  -- Moderate drizzle -> Drizzle, not freezing, intermittent - moderate
    ('open-meteo', '55', '54'),  -- Dense drizzle -> Drizzle, not freezing, intermittent - heavy
    ('open-meteo', '56', '56'),  -- Light freezing drizzle -> Drizzle, freezing, slight
    ('open-meteo', '57', '57'),  -- Dense freezing drizzle -> Drizzle, freezing, moderate or heavy

    -- Дождь (Rain)
    ('open-meteo', '61', '61'),  -- Slight rain -> Rain, not freezing, continuous - slight
    ('open-meteo', '63', '62'),  -- Moderate rain -> Rain, not freezing, intermittent - moderate
    ('open-meteo', '65', '64'),  -- Heavy rain -> Rain, not freezing, intermittent - heavy
    ('open-meteo', '66', '66'),  -- Light freezing rain -> Rain, freezing, slight
    ('open-meteo', '67', '67'),  -- Heavy freezing rain -> Rain, freezing, moderate or heavy

    -- Снег (Snow)
    ('open-meteo', '71', '71'),  -- Slight snow -> Continuous fall of snowflakes - slight
    ('open-meteo', '73', '72'),  -- Moderate snow -> Intermittent fall of snowflakes - moderate
    ('open-meteo', '75', '74'),  -- Heavy snow -> Intermittent fall of snowflakes - heavy
    ('open-meteo', '77', '77'),  -- Snow grains -> Snow grains

    -- Ливни (Showers)
    ('open-meteo', '80', '80'),  -- Slight rain showers -> Rain shower(s) - slight
    ('open-meteo', '81', '81'),  -- Moderate rain showers -> Rain shower(s) - moderate or heavy
    ('open-meteo', '82', '82'),  -- Violent rain showers -> Rain shower(s) - violent
    ('open-meteo', '85', '85'),  -- Slight snow showers -> Snow shower(s) - slight
    ('open-meteo', '86', '86'),  -- Heavy snow showers -> Snow shower(s) - moderate or heavy

    -- Гроза (Thunderstorm)
    ('open-meteo', '95', '95'),  -- Thunderstorm -> Thunderstorm, slight or moderate
    ('open-meteo', '96', '96'),  -- Thunderstorm with slight hail -> Thunderstorm, slight or moderate, with hail
    ('open-meteo', '99', '99')   -- Thunderstorm with heavy hail -> Thunderstorm, heavy, with hail
ON CONFLICT (provider_name, provider_code) DO NOTHING;