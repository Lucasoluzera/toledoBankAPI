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
                criarEndereco();
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


    @Test
    @DisplayName("Deve alterar um Endereco com sucesso.")
    void alterarEndereco() {
        Long id = 1L;

        Endereco enderecoAlterar = Endereco.builder()
                .id(id)
                .build();

        Endereco enderecoAlterado = criarEndereco();
        enderecoAlterado.setId(id);

        Mockito.when(repository.save(enderecoAlterar)).thenReturn(enderecoAlterado);

        Endereco endereco = service.update(enderecoAlterar);
        assertThat(endereco.getId()).isEqualTo(enderecoAlterado.getId());
        assertThat(endereco.getBairro()).isEqualTo(enderecoAlterado.getBairro());
        assertThat(endereco.getRua()).isEqualTo(enderecoAlterado.getRua());
        assertThat(endereco.getNumero()).isEqualTo(enderecoAlterado.getNumero());
        assertThat(endereco.getCep()).isEqualTo(enderecoAlterado.getCep());
        assertThat(endereco.getCidade()).isEqualTo(enderecoAlterado.getCidade());
        assertThat(endereco.getEstado()).isEqualTo(enderecoAlterado.getEstado());
    }

    private Endereco criarEndereco() {
        return Endereco.builder()
                .rua("Rua Para")
                .numero(975)
                .cep(16015283)
                .bairro("Jardim Sumaré")
                .cidade("Araçatuba")
                .estado("São Paulo")
                .build();
    }
}
