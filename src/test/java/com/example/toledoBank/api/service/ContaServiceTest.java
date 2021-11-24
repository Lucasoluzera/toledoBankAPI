package com.example.toledoBank.api.service;

import com.example.toledoBank.api.dto.ContaOperacoesDTO;
import com.example.toledoBank.api.model.Conta;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.repository.ContaRepository;
import com.example.toledoBank.api.repository.UsuarioRepository;
import com.example.toledoBank.api.service.impl.ContaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ContaServiceTest {

    ContaService service;

    @MockBean
    ContaRepository repository;

    @MockBean
    UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        this.service = new ContaServiceImpl(this.repository, this.usuarioRepository);
    }

    @Test
    @DisplayName("Deve salvar uma conta com sucesso.")
    void salvarConta() {
        Conta conta = criarContaBuilder();
        conta.setId(1L);

        BDDMockito.given(repository.save(Mockito.any(Conta.class))).willReturn(conta);

        Conta contaSalva = this.service.salvar(conta);

        assertThat(contaSalva.getId()).isNotNull();
        assertThat(contaSalva.getAgencia()).isEqualTo(12);
        assertThat(contaSalva.getNumero()).isEqualTo(1234);
        assertThat(contaSalva.getSaldo()).isEqualTo(BigDecimal.valueOf(10.25));
    }

    @Test
    @DisplayName("Deve sacar o saldo com sucesso.")
    void sacarSaldo() {
        ContaOperacoesDTO contaOperacoesDTO = criarContaOperacoesDTO();

        Conta conta = criarContaBuilder();
        conta.setId(1L);

        BDDMockito.given(repository.findByAgenciaAndNumero(Mockito.anyInt(), Mockito.anyInt())).willReturn(conta);

        BDDMockito.given(repository.save(Mockito.any(Conta.class))).willReturn(Conta.builder()
                .id(1L)
                .agencia(12)
                .numero(1234)
                .saldo(BigDecimal.valueOf(5.25))
                .build());

        ContaOperacoesDTO contaOperacoesDTOSalva = this.service.sacar(contaOperacoesDTO);

        assertThat(contaOperacoesDTOSalva.getConta().getId()).isNotNull();
        assertThat(contaOperacoesDTOSalva.getConta().getSaldo()).isEqualTo(BigDecimal.valueOf(5.25));
        assertThat(contaOperacoesDTOSalva.getSaldo()).isEqualTo("5");
        assertThat(contaOperacoesDTOSalva.getCpfContaSecundaria()).isEqualTo("1111111111");
    }

    @Test
    @DisplayName("Deve depositar o saldo com sucesso.")
    void depositarSaldo() {
        ContaOperacoesDTO contaOperacoesDTO = criarContaOperacoesDTO();

        Conta conta = criarContaBuilder();
        conta.setId(1L);

        BDDMockito.given(repository.findByAgenciaAndNumero(Mockito.anyInt(), Mockito.anyInt())).willReturn(conta);

        BDDMockito.given(repository.save(Mockito.any(Conta.class))).willReturn(Conta.builder()
                .id(1L)
                .agencia(12)
                .numero(1234)
                .saldo(BigDecimal.valueOf(15.25))
                .build());

        ContaOperacoesDTO contaOperacoesDTOSalva = this.service.depositar(contaOperacoesDTO);

        assertThat(contaOperacoesDTOSalva.getConta().getId()).isNotNull();
        assertThat(contaOperacoesDTOSalva.getConta().getSaldo()).isEqualTo(BigDecimal.valueOf(15.25));
        assertThat(contaOperacoesDTOSalva.getSaldo()).isEqualTo("5");
        assertThat(contaOperacoesDTOSalva.getCpfContaSecundaria()).isEqualTo("1111111111");
    }

    @Test
    @DisplayName("Deve transferir o saldo com sucesso.")
    void transferirSaldo() {
        ContaOperacoesDTO contaOperacoesDTO = criarContaOperacoesDTO();
        Conta conta = criarContaBuilder();
        conta.setId(1L);

        Conta contaSecundaria = Conta.builder()
                .id(2L)
                .agencia(11)
                .numero(12345)
                .saldo(BigDecimal.valueOf(0))
                .build();

        BDDMockito.given(repository.findByAgenciaAndNumero(Mockito.anyInt(), Mockito.anyInt())).willReturn(conta);

        BDDMockito.given(usuarioRepository.findByLogin(Mockito.any(String.class))).willReturn(Usuario.builder()
                .id(1L)
                .login("1111111111")
                .conta(contaSecundaria)
                .build());


        BDDMockito.given(repository.save(Mockito.any(Conta.class))).willReturn(Conta.builder()
                .id(1L)
                .agencia(12)
                .numero(1234)
                .saldo(BigDecimal.valueOf(5.25))
                .build(),
                Conta.builder()
                        .id(2L)
                        .agencia(11)
                        .numero(12345)
                        .saldo(BigDecimal.valueOf(5))
                        .build()
                );

        ContaOperacoesDTO contaOperacoesDTOSalva = this.service.transferir(contaOperacoesDTO);

        assertThat(contaOperacoesDTOSalva.getConta().getId()).isNotNull();
        assertThat(contaOperacoesDTOSalva.getConta().getSaldo()).isEqualTo(BigDecimal.valueOf(5.25));
        assertThat(contaOperacoesDTOSalva.getSaldo()).isEqualTo("5");
        assertThat(contaOperacoesDTOSalva.getCpfContaSecundaria()).isEqualTo("1111111111");
    }


    private Conta criarContaBuilder() {
        return Conta.builder()
                .agencia(12)
                .numero(1234)
                .saldo(BigDecimal.valueOf(10.25))
                .build();
    }

    private ContaOperacoesDTO criarContaOperacoesDTO() {
        return ContaOperacoesDTO.builder()
                .conta(criarContaBuilder())
                .cpfContaSecundaria("1111111111")
                .saldo("5")
                .build();
    }
}
