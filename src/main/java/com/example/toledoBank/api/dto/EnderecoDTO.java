package com.example.toledoBank.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnderecoDTO {
    Long id;
    String Rua;
    int numero;
    int cep;
    String bairro;
    String cidade;
}
