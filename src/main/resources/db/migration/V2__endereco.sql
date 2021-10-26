create table if not exists endereco(
    id bigserial not null primary key,
    rua varchar,
    numero integer,
    cep integer,
    bairro varchar,
    cidade varchar,
    estado varchar
)