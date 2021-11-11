package com.example.toledoBank.api.service.impl;

import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.repository.EnderecoRepository;
import com.example.toledoBank.api.service.EnderecoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoServiceImpl implements EnderecoService {

    private EnderecoRepository repository;

    public EnderecoServiceImpl(EnderecoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Endereco save(Endereco endereco) {
        return this.repository.save(endereco);
    }

    @Override
    public Endereco update(Endereco enderecoAtualizar) {
        if(enderecoAtualizar == null || enderecoAtualizar.getId() == null)
         throw new IllegalArgumentException("Endereço não pode ser nullo.");

        return repository.save(enderecoAtualizar);
    }
}
