package com.example.toledoBank.api.service;

import com.example.toledoBank.api.model.Telefone;

public interface TelefoneService {


    Telefone save(Telefone telefone);

    Telefone update(Telefone telefone);

    void excluir(Long id);
}
