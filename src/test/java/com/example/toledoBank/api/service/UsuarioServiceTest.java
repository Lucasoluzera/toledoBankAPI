package com.example.toledoBank.api.service;

import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.enums.TipoPessoa;
import com.example.toledoBank.api.model.Pessoa;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.repository.UsuarioRepository;
import com.example.toledoBank.api.service.impl.UsuarioServiceImpl;
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

    @BeforeEach
    public void setUp() {
        this.service = new UsuarioServiceImpl(this.repository, this.pessoaService);
    }

    @Test
    @DisplayName("Deve salvar um usuário com sucesso")
    void salvarUsuario() {
        UsuarioDTO usuarioDTO = criarUsuarioDTO();

        String senha = new BCryptPasswordEncoder().encode(usuarioDTO.getSenha());

        Usuario usuario = Usuario
                .builder()
                .id(1L)
                .senha(senha)
                .login("50336912870")
                .pessoa(criarPessoaObj())
                .build();


        Pessoa pessoaSalva = criarPessoaObj();
        pessoaSalva.setId(1L);

        BDDMockito.given(repository.save(Mockito.any(Usuario.class))).willReturn(usuario);

        BDDMockito.given(pessoaService.buscarPorCPF(Mockito.any(String.class))).willReturn(pessoaSalva);

        BDDMockito.given(pessoaService.existePorCPF(Mockito.any(String.class))).willReturn(true);

//        Mockito.when(repository.save(usuario))
//                .thenReturn(
//                        Usuario.builder()
//                        .id(1L)
//                        .senha(senha)
//                        .login("50336912870")
//                                .pessoa(criarPessoaObj())
//                        .build());

        UsuarioDTO usuarioDTOSalvo = service.save(usuarioDTO);

        assertThat(usuarioDTOSalvo.getId()).isNotNull();
        assertThat(usuarioDTOSalvo.getLogin()).isEqualTo("50336912870");
        assertThat(usuarioDTOSalvo.getCpfCnpj()).isEqualTo("50336912870");
        assertThat(usuarioDTOSalvo.getSenha()).isEqualTo("05/02/2001");
        assertThat(pessoaSalva.getId()).isNotNull();


    }

    private UsuarioDTO criarUsuarioDTO() {
        return UsuarioDTO.builder()
                .nomeRazaoSocial("Lucas Azevedo Souza")
                .login("50336912870")
                .cpfCnpj("50336912870")
                .senha("05/02/2001")
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

}
