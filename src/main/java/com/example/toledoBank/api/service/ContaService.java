package com.example.toledoBank.api.service;

import com.example.toledoBank.api.dto.ContaOperacoesDTO;
import com.example.toledoBank.api.model.Conta;
import com.example.toledoBank.api.model.Usuario;

import java.math.BigDecimal;

public interface ContaService {
    Conta salvar();

    ContaOperacoesDTO sacar(ContaOperacoesDTO contaOperacoesDTO);

    ContaOperacoesDTO depositar(ContaOperacoesDTO contaOperacoesDTO);

    ContaOperacoesDTO transferir(ContaOperacoesDTO contaOperacoesDTO);
}
