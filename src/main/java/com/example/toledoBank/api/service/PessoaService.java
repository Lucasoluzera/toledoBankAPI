package com.example.toledoBank.api.service;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.model.Telefone;

public interface PessoaService {


    PessoaDTO save(PessoaDTO pessoaDTO);

    Telefone salvarTelefone(String telefone);

    Endereco salvarEndereco(Endereco endereco);
}
