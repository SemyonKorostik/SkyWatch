create table if not exists location
(
    id               bigserial
        constraint location_pk
            primary key,
    latitude         numeric(10, 8) not null,
    longitude        numeric(11, 8) not null,
    time_zone_offset smallint       not null,
    country          varchar(255)   not null,
    place_name       varchar(255)   not null,
    constraint latitude_longitude_uk
        unique (latitude, longitude)
);

comment on table location is 'The table contains data on the object''s location.';

comment on column location.id is 'Primary key';

comment on column location.latitude is 'Latitude coordinate';

comment on column location.longitude is 'Longitude coordinate';

alter table location
    owner to postgres;

create trigger update_location_updated_at
    before update
    on location
    for each row
execute procedure update_updated_at_column();

create table if not exists "User"
(
    id          bigint  default nextval('user_id_seq'::regclass) not null
        constraint user_id_uindex
            primary key,
    location_id bigint
        constraint user_location_id_fk
            references location,
    login       varchar(254)                                     not null,
    language    char(2) default 'EN'::bpchar                     not null,
    chat_id     bigint
        constraint user_pk
            unique
);

comment on table "User" is 'Table containing user data';

comment on column "User".id is 'Primary key';

comment on column "User".login is 'User login';

alter table "User"
    owner to postgres;

create table if not exists weather_provider
(
    name       varchar(255)         not null
        primary key,
    api_url    varchar(255)         not null,
    priority   integer default 1    not null,
    is_enabled boolean default true not null
);

comment on table weather_provider is 'Weather provider directory';

comment on column weather_provider.name is 'Weather provider''s name';

comment on column weather_provider.api_url is 'Weather provider''s API URL';

comment on column weather_provider.priority is 'Weather provider''s priority. 1 - highest';

comment on column weather_provider.is_enabled is 'Weather provider''s availability';

alter table weather_provider
    owner to postgres;

create table if not exists wmo_weather_codes
(
    id            serial
        primary key,
    code          varchar(2)  not null
        unique,
    description   text        not null,
    category      varchar(50) not null,
    subcategory   varchar(100),
    emoji         varchar(10) not null,
    intensity     varchar(20),
    weather_group varchar(30) not null,
    created_at    timestamp default CURRENT_TIMESTAMP
);

alter table wmo_weather_codes
    owner to postgres;

create table if not exists weather_conditions
(
    id            bigserial
        primary key,
    provider_name varchar(255) not null
        constraint weather_conditions_provider_name_fk
            references weather_provider
            on update restrict on delete restrict,
    provider_code varchar(10)  not null,
    wmo_code      varchar(2)   not null
        constraint weather_conditions_wmo_weather_codes_code_fk
            references wmo_weather_codes (code),
    constraint provider_name_code_uq
        unique (provider_name, provider_code)
);

comment on table weather_conditions is 'Weather conditions directory';

comment on column weather_conditions.id is 'Primary key';

comment on column weather_conditions.provider_name is 'Weather symbol provider';

comment on column weather_conditions.provider_code is 'Weather code';

comment on column weather_conditions.wmo_code is 'Weather description';

alter table weather_conditions
    owner to postgres;

create index if not exists idx_weather_conditions_provider_code
    on weather_conditions (provider_name, provider_code);

create table if not exists daily_weather
(
    id                   bigserial
        primary key,
    location_id          bigint        not null
        constraint daily_weather_location_id_fk
            references location
            on update restrict on delete restrict,
    date                 date          not null,
    weather_condition_id bigint        not null
        constraint daily_weather_weather_condition_id_weather_provider_name_fk
            references weather_conditions
            on update restrict on delete restrict,
    temperature_max      numeric(4, 1) not null,
    temperature_min      numeric(4, 1) not null,
    created_at           timestamp default CURRENT_TIMESTAMP,
    updated_at           timestamp default CURRENT_TIMESTAMP
);

comment on table daily_weather is 'Weather characteristics for the day';

comment on column daily_weather.id is 'Primary key';

comment on column daily_weather.location_id is 'Related location';

comment on column daily_weather.date is 'Weather forecast date in iso8601 format with timezone';

comment on column daily_weather.weather_condition_id is 'Related weather condition';

comment on column daily_weather.temperature_max is 'Maximum temperature during the day';

comment on column daily_weather.temperature_min is 'Minimum temperature during the day';

comment on column daily_weather.created_at is 'Creation timestamp';

comment on column daily_weather.updated_at is 'Last update timestamp';

alter table daily_weather
    owner to postgres;

create index if not exists idx_daily_weather_location_id_date
    on daily_weather (location_id, date);

create index if not exists idx_daily_weather_date
    on daily_weather (date);

create index if not exists idx_daily_weather_location_id
    on daily_weather (location_id);

create trigger update_daily_weather_updated_at
    before update
    on daily_weather
    for each row
execute procedure update_updated_at_column();

create table if not exists hourly_weather
(
    time                      timestamp with time zone not null,
    daily_weather_id          bigint                   not null
        constraint hourly_weather_daily_weather_id_fk
            references daily_weather
            on update restrict on delete restrict,
    weather_condition_id      bigint                   not null
        constraint hourly_weather_weather_condition_id_fk
            references weather_conditions
            on update restrict on delete restrict,
    temperature               numeric(4, 1)            not null,
    humidity                  smallint                 not null,
    precipitation             numeric(5, 2),
    precipitation_probability smallint,
    wind_speed                numeric(5, 2),
    wind_direction            smallint,
    pressure                  numeric(6, 2),
    created_at                timestamp default CURRENT_TIMESTAMP,
    updated_at                timestamp default CURRENT_TIMESTAMP,
    primary key (time, daily_weather_id)
);

comment on table hourly_weather is 'Weather characteristics for the hour';

comment on column hourly_weather.time is 'Weather forecast time in iso8601 format with timezone';

comment on column hourly_weather.daily_weather_id is 'Corresponding forecast day';

comment on column hourly_weather.weather_condition_id is 'Related weather condition';

comment on column hourly_weather.temperature is 'Temperature in celsius';

comment on column hourly_weather.humidity is 'Humidity in percent';

comment on column hourly_weather.precipitation is 'Precipitation amount in mm';

comment on column hourly_weather.precipitation_probability is 'Precipitation probability in percent';

comment on column hourly_weather.wind_speed is 'Wind speed in km/h';

comment on column hourly_weather.wind_direction is 'Wind direction in degrees';

comment on column hourly_weather.pressure is 'Surface pressure in hPa';

comment on column hourly_weather.created_at is 'Creation timestamp';

comment on column hourly_weather.updated_at is 'Last update timestamp';

alter table hourly_weather
    owner to postgres;

create index if not exists idx_hourly_weather_daily_weather_id
    on hourly_weather (daily_weather_id);

create index if not exists idx_hourly_weather_time
    on hourly_weather (time);

create index if not exists idx_hourly_weather_daily_time
    on hourly_weather (daily_weather_id, time);

create trigger update_hourly_weather_updated_at
    before update
    on hourly_weather
    for each row
execute procedure update_updated_at_column();

create index if not exists idx_wmo_code
    on wmo_weather_codes (code);

