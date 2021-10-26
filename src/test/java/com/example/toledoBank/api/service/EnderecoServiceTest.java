package com.example.toledoBank.api.service;

import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.repository.EnderecoRepository;
import com.example.toledoBank.api.service.impl.EnderecoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EnderecoServiceTest {

    EnderecoService service;
    @MockBean
    private EnderecoRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new EnderecoServiceImpl(this.repository);
    }

    @Test
    @DisplayName("Deve salvar um Endereço com sucesso.")
    void salvarEndereco() {
        Endereco endereco =
                Endereco.builder()
                        .rua("Rua Para")
                        .numero(975)
                        .cep(16015283)
                        .bairro("Jardim Sumaré")
                        .cidade("Araçatuba")
                        .estado("São Paulo")
                        .build();
        Mockito.when(repository.save(endereco))
                .thenReturn(
                        Endereco.builder()
                                .id(1L)
                                .rua("Rua Para")
                                .numero(975)
                                .cep(16015283)
                                .bairro("Jardim Sumaré")
                                .cidade("Araçatuba")
                                .estado("São Paulo")
                                .build());

        Endereco enderecoSalvo = service.save(endereco);

        assertThat(enderecoSalvo.getId()).isNotNull();
        assertThat(enderecoSalvo.getBairro()).isEqualTo("Jardim Sumaré");
        assertThat(enderecoSalvo.getRua()).isEqualTo("Rua Para");
        assertThat(enderecoSalvo.getNumero()).isEqualTo(975);
        assertThat(enderecoSalvo.getCep()).isEqualTo(16015283);
        assertThat(enderecoSalvo.getCidade()).isEqualTo("Araçatuba");
        assertThat(enderecoSalvo.getEstado()).isEqualTo("São Paulo");
    }
}
