-- Project Inception (Requirements.md §22, FR-102..FR-107)

create table project (
    id           uuid primary key,
    project_key  varchar(10)  not null unique,
    name         varchar(200) not null,
    status       varchar(20)  not null,
    product_type varchar(30)  not null,
    created_by   varchar(100) not null,
    created_at   timestamptz  not null,
    updated_at   timestamptz  not null
);

create table charter_revision (
    id                 uuid primary key,
    project_id         uuid         not null references project (id),
    revision           int          not null,
    parent_revision_id uuid references charter_revision (id),
    content            jsonb        not null,
    markdown           text         not null,
    actor              varchar(100) not null,
    source             varchar(30)  not null, -- INCEPTION | SETTINGS | AI_CHAT
    agent_ref          varchar(200),
    created_at         timestamptz  not null,
    unique (project_id, revision)
);

create table open_question (
    id         uuid primary key,
    project_id uuid        not null references project (id),
    question   text        not null,
    status     varchar(20) not null,
    created_at timestamptz not null
);

create table inception_conversation (
    project_id uuid primary key references project (id),
    transcript jsonb        not null,
    agent_ref  varchar(200),
    created_at timestamptz  not null
);

create table audit_event (
    id         uuid primary key,
    project_id uuid references project (id),
    actor      varchar(100) not null,
    action     varchar(50)  not null,
    details    jsonb        not null,
    created_at timestamptz  not null
);

create index audit_event_project_idx on audit_event (project_id, created_at desc);
