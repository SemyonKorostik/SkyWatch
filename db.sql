create sequence user_id_seq;

alter sequence user_id_seq owner to postgres;

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

alter sequence user_id_seq owned by "User".id;

