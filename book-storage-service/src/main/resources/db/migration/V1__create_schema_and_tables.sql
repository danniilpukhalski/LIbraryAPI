drop schema if exists library_schema cascade ;
create schema if not exists library_schema;

create table library_schema.trackers
(
    id          bigserial primary key,
    isbn        varchar(255) not null,
    title       varchar(255) not null,
    genre       varchar(255) not null,
    description text,
    author      varchar(255) not null,
    deleted     bool         not null

);

create table library_schema.users
(
    id       bigserial primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    deleted  bool         not null

);

create table library_schema.users_roles
(
    user_id bigint       not null,
    roles   varchar(255) not null,
    primary key (user_id, roles),
    constraint fk_users_roles_users foreign key (user_id) references library_schema.users (id) on delete cascade on update no action
);

create table library_schema.bookTrack
(
    id       bigserial primary key,
    bookId   bigint      not null unique,
    status   varchar(50) not null,
    taken    date,
    returned date,
    deleted  bool        not null,
    constraint fk_book foreign key (bookId) references library_schema.trackers (id) on delete cascade
);



