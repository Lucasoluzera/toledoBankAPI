package com.example.toledoBank.api.service;

import com.example.toledoBank.api.model.Endereco;

import java.util.Optional;

public interface EnderecoService {

    Endereco save(Endereco endereco);

    Optional<Endereco> getById(Long id);

    Endereco update(Endereco enderecoAtualizar);
}
