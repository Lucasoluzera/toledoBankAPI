package com.example.toledoBank.api.service;

import com.example.toledoBank.api.dto.ContaOperacoesDTO;
import com.example.toledoBank.api.model.Conta;

import java.math.BigDecimal;

public interface ContaService {
    Conta salvar(Conta conta);

    ContaOperacoesDTO sacar(ContaOperacoesDTO contaOperacoesDTO);

    ContaOperacoesDTO depositar(ContaOperacoesDTO contaOperacoesDTO);

    ContaOperacoesDTO transferir(ContaOperacoesDTO contaOperacoesDTO);
}
