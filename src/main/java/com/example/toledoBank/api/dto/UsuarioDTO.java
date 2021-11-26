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
public class UsuarioDTO {

    private Long id;
    private String nomeRazaoSocial;
    private String cpfCnpj;
    private Conta conta;
    private PessoaDTO pessoaDTO;
    private Boolean contaAdmin;
    private String login;
    private String senha;
    private String token;
}
