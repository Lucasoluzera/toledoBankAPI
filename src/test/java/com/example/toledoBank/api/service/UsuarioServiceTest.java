package com.example.toledoBank.api.service;

import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.repository.UsuarioRepository;
import com.example.toledoBank.api.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    UsuarioService service;

    @MockBean
    UsuarioRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new UsuarioServiceImpl(this.repository);
    }

    @Test
    @DisplayName("Deve salvar um usuário com sucesso")
    void salvarUsuario() {
        Usuario usuario = Usuario
                .builder()
                .senha("05/02/2001")
                .login("50336912870")
                .build();

        String senha = new BCryptPasswordEncoder().encode(usuario.getSenha());

        Mockito.when(repository.save(usuario))
                .thenReturn(
                        Usuario.builder()
                        .id(1L)
                        .senha(senha)
                        .login("50336912870")
                        .build());

        Usuario usuarioSalvo = service.save(usuario);

        assertThat(usuarioSalvo.getId()).isNotNull();
        assertThat(usuarioSalvo.getLogin()).isEqualTo("50336912870");
        assertThat(usuarioSalvo.getSenha()).isEqualTo(senha);

    }
}
