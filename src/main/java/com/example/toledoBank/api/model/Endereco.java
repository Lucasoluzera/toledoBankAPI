package com.example.toledoBank.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    String rua;

    @Column
    int numero;

    @Column
    int cep;

    @Column
    String bairro;

    @Column
    String cidade;

    @Column
    String estado;
}
