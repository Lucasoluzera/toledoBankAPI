package com.example.toledoBank.api.service;

import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.model.Telefone;
import com.example.toledoBank.api.repository.TelefoneRepository;
import com.example.toledoBank.api.service.impl.TelefoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TelefoneServiceTest {

    private TelefoneService service;

    @MockBean
    private TelefoneRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new TelefoneServiceImpl(this.repository);
    }


    @Test
    @DisplayName("Deve salvar um telefone.")
    void salvarTelefone() {
        Telefone telefone =
                criarTelefone();

        Mockito.when(repository.save(telefone))
                .thenReturn(
                                Telefone.builder()
                                        .id(1L)
                                        .numero("996230715")
                                        .build());

        Telefone telefoneSalvo = service.save(telefone);

        assertThat(telefoneSalvo.getId()).isNotNull();
        assertThat(telefoneSalvo.getNumero()).isEqualTo("996230715");
    }


    @Test
    @DisplayName("Deve alterar um telefone.")
    void alterarTelefone() {
        Long id = 1L;

        Telefone telefoneAlterar = Telefone.builder()
                .id(id)
                .build();

        Telefone telefoneAlterado = criarTelefone();
        telefoneAlterado.setId(id);

        Mockito.when(repository.save(telefoneAlterar)).thenReturn(telefoneAlterado);

        Telefone telefone = service.update(telefoneAlterar);

        assertThat(telefone.getId()).isEqualTo(telefoneAlterado.getId());
        assertThat(telefone.getNumero()).isEqualTo(telefoneAlterado.getNumero());
    }


    private Telefone criarTelefone() {
        return Telefone.builder()
                .numero("996230715")
                .build();
    }
}
