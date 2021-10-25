create table if not exists usuario(
    id bigserial not null primary key,
    nome varchar,
    cpf varchar,
    senha varchar
)