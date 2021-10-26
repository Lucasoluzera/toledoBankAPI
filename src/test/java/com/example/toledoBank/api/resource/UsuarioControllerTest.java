package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.seguranca.JwtAuthenticationEntryPoint;
import com.example.toledoBank.api.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false) // coloco isso pra não fazer a autenticação
@ContextConfiguration(classes = {JwtAuthenticationEntryPoint.class}) // Carrego esse cara para fazer o EntryPoint
@Import({ModelMapper.class, UsuarioController.class}) // o Import no ModelMapper é explicável.. o Import do UsuarioController tá estranho.. não deveria ser assim..
public class UsuarioControllerTest {

    static String URL = "/usuario";

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService service;

    @Test
    @DisplayName("Deve criar um usuário comum com sucesso.")
    void criarUsuario() throws Exception {

        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .nome("Lucas Azevedo Souza")
                .login("50336912870")
                .senha("05/02/2001")
                .build();

        Usuario usuario = Usuario.builder()
                .id(1L)
                .login("50336912870")
                .senha("05/02/2001")
                .build();

        BDDMockito.given(service.save(Mockito.any(Usuario.class))).willReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(usuarioDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect( jsonPath("id").isNotEmpty())
//                .andExpect( jsonPath("nome").value(usuarioDTO.getNome()))
                .andExpect( jsonPath("login").value(usuarioDTO.getLogin()))
                .andExpect( jsonPath("senha").value(usuarioDTO.getSenha()));

    }
}
