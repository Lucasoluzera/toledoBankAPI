package com.example.toledoBank.api.dto;

import com.example.toledoBank.api.model.Conta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaOperacoesDTO {

    Conta conta;
    String cpfContaSecundaria;
    String saldo;
}
