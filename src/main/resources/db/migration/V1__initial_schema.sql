create table users (
    id uuid primary key,
    name varchar(100) not null,
    email varchar(180) not null unique,
    password_hash varchar(100) not null,
    role varchar(20) not null,
    created_at timestamp with time zone not null
);

create table matches (
    id uuid primary key,
    title varchar(120) not null,
    sport varchar(40) not null,
    address varchar(240) not null,
    starts_at timestamp with time zone not null,
    capacity integer not null check (capacity between 2 and 100),
    status varchar(20) not null,
    organizer_id uuid not null references users(id),
    version bigint not null default 0,
    created_at timestamp with time zone not null
);

create table participations (
    id uuid primary key,
    match_id uuid not null references matches(id) on delete cascade,
    player_id uuid not null references users(id),
    joined_at timestamp with time zone not null,
    constraint uk_participation_match_player unique (match_id, player_id)
);

create index idx_matches_status_starts_at on matches(status, starts_at);
create index idx_participations_match on participations(match_id);
