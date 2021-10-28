create table if not exists telefone(
    id bigserial not null primary key,
    numero varchar,
    ddd integer
)