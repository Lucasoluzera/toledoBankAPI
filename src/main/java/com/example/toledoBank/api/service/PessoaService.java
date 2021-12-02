package com.example.toledoBank.api.service;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.model.Pessoa;
import com.example.toledoBank.api.model.Telefone;

import java.util.Optional;

public interface PessoaService {


    PessoaDTO salvar(PessoaDTO pessoaDTO);

    Telefone salvarTelefone(String telefone);

    Endereco salvarEndereco(Endereco endereco);

    Pessoa buscarPorCPF(String cpf);

    Boolean existePorCPF(String cpf);

    PessoaDTO alterar(PessoaDTO pessoaDTO);

    void excluir(Long id);

    Optional<Pessoa> buscarPessoaId(Long id);
}
