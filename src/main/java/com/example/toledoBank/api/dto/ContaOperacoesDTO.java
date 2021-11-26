package com.example.toledoBank.api.dto;

import com.example.toledoBank.api.model.Conta;
import com.example.toledoBank.api.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaOperacoesDTO {

    Usuario usuario;
    String cpfContaSecundaria;
    String saldo;
}
