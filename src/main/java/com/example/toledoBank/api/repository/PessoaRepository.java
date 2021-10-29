package com.example.toledoBank.api.repository;

import com.example.toledoBank.api.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
