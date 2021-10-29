create table if not exists pessoa(
    id bigserial not null primary key,
    nome_razao_social varchar,
    cpf_cnpj varchar,
    data_nascimento_abertura date,
    tipo_pessoa varchar,
    telefone_id bigint references telefone,
    endereco_id bigint references endereco
)