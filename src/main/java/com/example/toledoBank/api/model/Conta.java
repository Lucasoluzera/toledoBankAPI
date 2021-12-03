package com.example.toledoBank.api.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int numero;

    @Column
    int agencia;

    @Column
    BigDecimal saldo;
}
