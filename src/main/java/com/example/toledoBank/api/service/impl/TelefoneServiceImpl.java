package com.example.toledoBank.api.service.impl;

import com.example.toledoBank.api.model.Telefone;
import com.example.toledoBank.api.repository.TelefoneRepository;
import com.example.toledoBank.api.service.TelefoneService;
import org.springframework.stereotype.Service;

@Service
public class TelefoneServiceImpl implements TelefoneService {

    private TelefoneRepository repository;

    public TelefoneServiceImpl(TelefoneRepository repository) {
        this.repository = repository;
    }

    @Override
    public Telefone save(Telefone telefone) {
        return this.repository.save(telefone);
    }

    @Override
    public Telefone update(Telefone telefone) {
        if(telefone == null || telefone.getId() == null)
            throw new IllegalArgumentException("Telefone não pode ser nullo");

        return repository.save(telefone);
    }

    @Override
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
