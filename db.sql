CREATE OR REPLACE FUNCTION populate_geog_on_insert()
    RETURNS TRIGGER AS $$
BEGIN
    -- This calculates the geography point using longitude and latitude
    -- 4326 is the standard WGS 84 spatial reference identifier (SRID)
    NEW.geog := ST_SetSRID(ST_MakePoint(NEW.longitude, NEW.latitude), 4326)::geography;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_populate_geog
    BEFORE INSERT ON location
    FOR EACH ROW
EXECUTE FUNCTION populate_geog_on_insert();

create function update_updated_at_column() returns trigger
    language plpgsql
as
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;

alter function update_updated_at_column() owner to postgres;


create table if not exists location
(
    id           bigserial
        constraint location_pk
            primary key,
    latitude     numeric(10, 8)         not null,
    longitude    numeric(11, 8)         not null,
    time_zone_id varchar(255)           not null,
    country      varchar(255)           not null,
    place_name   varchar(255)           not null,
    geog         geography(Point, 4326) not null,
    updated_at   timestamp,
    constraint latitude_longitude_uk
        unique (latitude, longitude)
);

create table spatial_ref_sys
(
    srid      integer not null
        primary key
        constraint spatial_ref_sys_srid_check
            check ((srid > 0) AND (srid <= 998999)),
    auth_name varchar(256),
    auth_srid integer,
    srtext    varchar(2048),
    proj4text varchar(2048)
);

create table location
(
    id           bigserial
        constraint location_pk
            primary key,
    latitude     numeric(10, 8)         not null,
    longitude    numeric(11, 8)         not null,
    time_zone_id varchar(255)           not null,
    country      varchar(255)           not null,
    place_name   varchar(255)           not null,
    geog         geography(Point, 4326) not null,
    updated_at   timestamp,
    constraint latitude_longitude_uk
        unique (latitude, longitude)
);

comment on table location is 'The table contains data on the object''s location.';

comment on column location.id is 'Primary key';

comment on column location.latitude is 'Latitude coordinate';

comment on column location.longitude is 'Longitude coordinate';

create trigger update_location_updated_at
    before update
    on location
    for each row
execute procedure update_updated_at_column();

create trigger trg_populate_geog
    before insert
    on location
    for each row
execute procedure populate_geog_on_insert();

create table "User"
(
    location_id bigint
        constraint user_location_id_fk
            references location,
    login       varchar(254)                                     not null,
    language    char(2) default 'EN'::bpchar                     not null,
    chat_id     bigint
        constraint chat_uk
            unique,
    id          bigint  default nextval('user_id_seq'::regclass) not null
        constraint user_pk
            unique
);

comment on table "User" is 'Table containing user data';

comment on column "User".login is 'User login';

create table weather_provider
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

create table wmo_weather_codes
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

create index idx_wmo_code
    on wmo_weather_codes (code);

create table weather_conditions
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

create index idx_weather_conditions_provider_code
    on weather_conditions (provider_name, provider_code);

create table daily_weather
(
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
    updated_at           timestamp default CURRENT_TIMESTAMP,
    constraint daily_weather_pk
        primary key (location_id, date)
);

comment on table daily_weather is 'Weather characteristics for the day';

comment on column daily_weather.location_id is 'Related location';

comment on column daily_weather.date is 'Weather forecast date in iso8601 format with timezone';

comment on column daily_weather.weather_condition_id is 'Related weather condition';

comment on column daily_weather.temperature_max is 'Maximum temperature during the day';

comment on column daily_weather.temperature_min is 'Minimum temperature during the day';

comment on column daily_weather.created_at is 'Creation timestamp';

comment on column daily_weather.updated_at is 'Last update timestamp';

create index idx_daily_weather_location_id_date
    on daily_weather (location_id, date);

create index idx_daily_weather_date
    on daily_weather (date);

create index idx_daily_weather_location_id
    on daily_weather (location_id);

create trigger update_daily_weather_updated_at
    before update
    on daily_weather
    for each row
execute procedure update_updated_at_column();

create table hourly_weather
(
    time                      timestamp with time zone not null,
    location_id               bigint                   not null
        constraint hourly_weather_location_id_fk
            references location
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
    primary key (time, location_id)
);

comment on table hourly_weather is 'Weather characteristics for the hour';

comment on column hourly_weather.time is 'Weather forecast time in iso8601 format with timezone';

comment on column hourly_weather.location_id is 'Corresponding forecast day';

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

create index idx_hourly_weather_daily_weather_id
    on hourly_weather (location_id);

create index idx_hourly_weather_time
    on hourly_weather (time);

create index idx_hourly_weather_daily_time
    on hourly_weather (location_id, time);

create trigger update_hourly_weather_updated_at
    before update
    on hourly_weather
    for each row
execute procedure update_updated_at_column();

create table current_weather
(
    location_id          bigint not null
        constraint current_weather_pk
            primary key,
    weather_condition_id bigint
        constraint current_weather_weather_conditions_id_fk
            references weather_conditions,
    temperature          double precision,
    wind_speed           double precision,
    wind_direction       integer,
    pressure             double precision,
    created_at           timestamp,
    updated_at           timestamp,
    humidity             integer
);

create function update_created_at_column() returns trigger
    language plpgsql
as
$$
BEGIN
    NEW.created_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;

alter function update_created_at_column() owner to postgres;

create trigger update_daily_weather_created_at
    before insert
    on daily_weather
    for each row
execute procedure update_created_at_column();

create trigger update_current_weather_created_at
    before insert
    on current_weather
    for each row
execute procedure update_created_at_column();

create trigger update_hourly_weather_created_at
    before insert
    on hourly_weather
    for each row
execute procedure update_created_at_column();

create trigger update_location_created_at
    before insert
    on location
    for each row
execute procedure update_created_at_column();

