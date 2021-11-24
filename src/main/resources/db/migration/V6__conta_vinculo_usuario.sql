create table if not exists conta(
    id bigserial not null primary key,
    numero int,
    agencia int,
    saldo NUMERIC(19,2)
);

ALTER TABLE usuario add column if not exists conta_id bigint references conta