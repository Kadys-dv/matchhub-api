create table moderation_reports (
    id uuid primary key,
    reporter_id uuid not null references users(id),
    match_id uuid references matches(id) on delete set null,
    reason varchar(80) not null,
    details varchar(1000) not null,
    status varchar(20) not null,
    created_at timestamp with time zone not null,
    resolved_at timestamp with time zone
);

create index idx_moderation_reports_status_created on moderation_reports(status, created_at desc);
