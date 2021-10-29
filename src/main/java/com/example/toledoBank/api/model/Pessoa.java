package com.example.toledoBank.api.model;

import com.example.toledoBank.api.enums.TipoPessoa;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pessoa {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "nome_razao_social")
    String nomeRazaoSocial;

    @Column(name = "cpf_cnpj")
    String cpfCnpj;

    @Column(name = "data_nascimento_abertura")
    LocalDate dataNascAbertura;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "telefone_id")
    Telefone telefone;

    @Column(name = "tipo_pessoa")
    @Enumerated(EnumType.STRING)
    TipoPessoa tipoPessoa;

}
