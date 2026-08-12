alter table users add column enabled boolean not null default true;

create index idx_users_created_at on users(created_at desc);
create index idx_matches_created_at on matches(created_at desc);
create index idx_participations_player on participations(player_id);
