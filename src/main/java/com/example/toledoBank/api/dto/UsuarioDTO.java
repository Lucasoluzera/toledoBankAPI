package com.example.toledoBank.api.dto;

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
    private String login;
    private String senha;
    private String token;
}
