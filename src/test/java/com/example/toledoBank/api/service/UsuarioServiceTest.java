package com.example.toledoBank.api.service;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.enums.TipoPessoa;
import com.example.toledoBank.api.model.Conta;
import com.example.toledoBank.api.model.Pessoa;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.repository.UsuarioRepository;
import com.example.toledoBank.api.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    UsuarioService service;

    @MockBean
    UsuarioRepository repository;

    @MockBean
    PessoaService pessoaService;

    @MockBean
    ContaService contaService;

    @BeforeEach
    public void setUp() {
        this.service = new UsuarioServiceImpl(this.repository, this.pessoaService, this.contaService);
    }

    @Test
    @DisplayName("Deve salvar um usuário comum com sucesso quando tiver o CPF")
    void salvarUsuarioPorCPF() {
        UsuarioDTO usuarioDTO = criarUsuarioComumDTO();
        usuarioDTO.setPessoaDTO(null);
        usuarioDTO.setContaAdmin(false);
        String senha = new BCryptPasswordEncoder().encode(usuarioDTO.getSenha());

        Usuario usuarioSalvo = Usuario
                .builder()
                .id(1L)
                .senha(senha)
                .login("50336912870")
                .contaAdmin(false)
                .conta(criarConta())
                .pessoa(criarPessoaObj())
                .build();

        Conta contaSalva = criarConta();


        Pessoa pessoaSalva = criarPessoaObj();
        pessoaSalva.setId(1L);

        BDDMockito.given(repository.save(Mockito.any(Usuario.class))).willReturn(usuarioSalvo);

        BDDMockito.given(contaService.salvar()).willReturn(contaSalva);

        BDDMockito.given(pessoaService.buscarPorCPF(Mockito.any(String.class))).willReturn(pessoaSalva);

        BDDMockito.given(pessoaService.existePorCPF(Mockito.any(String.class))).willReturn(true);


        UsuarioDTO usuarioDTOSalvo = service.save(usuarioDTO);

        assertThat(usuarioDTOSalvo.getId()).isNotNull();
        assertThat(usuarioDTOSalvo.getLogin()).isEqualTo("50336912870");
        assertThat(usuarioDTOSalvo.getCpfCnpj()).isEqualTo("50336912870");
        assertThat(usuarioDTOSalvo.getSenha()).isEqualTo("05/02/2001");
        assertThat(usuarioDTOSalvo.getConta().getSaldo()).isNotNull();
        assertThat(usuarioDTOSalvo.getConta().getId()).isNotNull();
        assertThat(pessoaSalva.getId()).isNotNull();
    }

    private Conta criarConta() {
        return Conta.builder()
                .id(1L)
                .saldo(BigDecimal.ZERO)
                .agencia(1)
                .build();
    }

    @Test
    @DisplayName("Deve salvar um usuário comum com sucesso quando tiver a pessoa")
    void salvarUsuarioComPessoa() {
        UsuarioDTO usuarioDTO = criarUsuarioComumDTO();

        String senha = new BCryptPasswordEncoder().encode(usuarioDTO.getSenha());

        Conta contaSalva = criarConta();

        PessoaDTO pessoaSalvaDTO = criarPessoaDTO();
        pessoaSalvaDTO.setId(1L);

        Pessoa pessoaSalva = criarPessoaObj();
        pessoaSalva.setId(1L);

        Usuario usuarioSalvo = Usuario
                .builder()
                .id(1L)
                .senha(senha)
                .login("50336912870")
                .conta(contaSalva)
                .contaAdmin(false)
                .pessoa(pessoaSalva)
                .build();

        BDDMockito.given(repository.save(Mockito.any(Usuario.class))).willReturn(usuarioSalvo);

        BDDMockito.given(pessoaService.save(Mockito.any(PessoaDTO.class))).willReturn(pessoaSalvaDTO);

        BDDMockito.given(pessoaService.buscarPorCPF(Mockito.any(String.class))).willReturn(pessoaSalva);

        BDDMockito.given(contaService.salvar()).willReturn(contaSalva);

        UsuarioDTO usuarioDTOSalvo = service.save(usuarioDTO);

        assertThat(usuarioDTOSalvo.getId()).isNotNull();
        assertThat(usuarioDTOSalvo.getPessoaDTO().getId()).isNotNull();
        assertThat(usuarioDTOSalvo.getLogin()).isEqualTo("50336912870");
        assertThat(usuarioDTOSalvo.getCpfCnpj()).isEqualTo("50336912870");
        assertThat(usuarioDTOSalvo.getConta().getSaldo()).isEqualTo(BigDecimal.ZERO);
        assertThat(usuarioDTOSalvo.getConta().getId()).isNotNull();
        assertThat(usuarioDTOSalvo.getSenha()).isEqualTo("05/02/2001");
    }

    @Test
    @DisplayName("Não deve salvar um usuário caso não tenha pessoa ou CPF encontrado.")
    void naoDeveSalvarUsuarioSemPessoa() {
        UsuarioDTO usuarioDTO = criarUsuarioComumDTO();

        usuarioDTO.setCpfCnpj(null);
        usuarioDTO.setPessoaDTO(null);

        BDDMockito.given(pessoaService.existePorCPF(Mockito.any(String.class))).willReturn(false);


        Throwable exception = Assertions.catchThrowable(() -> service.save(usuarioDTO));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Não existe uma pessoa com esse CPF.");

        Mockito.verify(repository, Mockito.never()).save(new Usuario());


    }

    private UsuarioDTO criarUsuarioComumDTO() {
        return UsuarioDTO.builder()
                .nomeRazaoSocial("Lucas Azevedo Souza")
                .login("50336912870")
                .cpfCnpj("50336912870")
                .senha("05/02/2001")
                .contaAdmin(false)
                .pessoaDTO(criarPessoaDTO())
                .build();
    }

    private Pessoa criarPessoaObj() {
        return Pessoa.builder()
                .nomeRazaoSocial("Lucas Azevedo Souza")
                .cpfCnpj("50336912870")
                .dataNascAbertura(LocalDate.of(2001, 2, 5))
                .tipoPessoa(TipoPessoa.FISICA)
                .endereco(null)
                .telefone(null)
                .build();
    }

    private PessoaDTO criarPessoaDTO() {
        return PessoaDTO.builder()
                .nomeRazaoSocial("Lucas Azevedo Souza")
                .cpfCnpj("50336912870")
                .dataNascAbertura(LocalDate.of(2001, 2, 5).toString())
                .tipoPessoa(TipoPessoa.FISICA)
                .endereco(null)
                .telefoneDTO("18996230715")
                .build();
    }

}
