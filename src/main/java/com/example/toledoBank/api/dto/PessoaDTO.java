package com.example.toledoBank.api.dto;

import com.example.toledoBank.api.enums.TipoPessoa;
import com.example.toledoBank.api.model.Endereco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaDTO {

    Long id;
    String nomeRazaoSocial;
    String cpfCnpj;
    String dataNascAbertura;
    Endereco endereco;
    String telefoneDTO;
    TipoPessoa tipoPessoa;
}
