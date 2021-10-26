create table if not exists usuario(
    id bigserial not null primary key,
    login varchar,
    senha varchar
)