package com.example.toledoBank.api.repository;

import com.example.toledoBank.api.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Conta findByAgenciaAndNumero(Integer agencia, Integer numero);

    Conta findFirstByOrderByIdDesc();
}
