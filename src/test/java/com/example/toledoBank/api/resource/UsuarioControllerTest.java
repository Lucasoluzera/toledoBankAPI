package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.enums.TipoPessoa;
import com.example.toledoBank.api.model.Conta;
import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.model.Pessoa;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false) // coloco isso pra não fazer a autenticação
@ContextConfiguration(classes = {JwtAuthenticationEntryPoint.class}) // Carrego esse cara para fazer o EntryPoint
@Import({ModelMapper.class, UsuarioController.class})
// o Import no ModelMapper é explicável.. o Import do UsuarioController tá estranho.. não deveria ser assim..
public class UsuarioControllerTest {

    static String URL = "/usuario";

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService service;

    @Test
    @DisplayName("Deve criar um usuário comum com sucesso.")
    void criarUsuario() throws Exception {

        UsuarioDTO usuarioDTO = criarUsuarioComumDTO();

        UsuarioDTO usuarioDTOSalvo = criarUsuarioComumDTO();
        usuarioDTOSalvo.setId(1L);
        usuarioDTOSalvo.setConta(criarContaBuilder());
        usuarioDTOSalvo.getConta().setId(1L);


        BDDMockito.given(service.save(Mockito.any(UsuarioDTO.class))).willReturn(usuarioDTOSalvo);


        String json = new ObjectMapper().writeValueAsString(usuarioDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("conta.id").isNotEmpty())
                .andExpect(jsonPath("conta.saldo").value(0))
                .andExpect( jsonPath("nomeRazaoSocial").value(usuarioDTO.getNomeRazaoSocial()))
                .andExpect( jsonPath("cpfCnpj").value(usuarioDTO.getCpfCnpj()))
                .andExpect(jsonPath("login").value(usuarioDTO.getLogin()))
                .andExpect(jsonPath("senha").value(usuarioDTO.getSenha()));

    }

    @Test
    @DisplayName("Deve Alterar um Usuario com sucesso.")
    void alterarUsuario() throws Exception {
        Long id = 1L;

        PessoaDTO pessoaSalva = PessoaDTO.builder()
                .id(id)
                .nomeRazaoSocial("teste")
                .cpfCnpj("50336912870")
                .dataNascAbertura(LocalDate.of(2001, 2, 5).toString())
                .tipoPessoa(TipoPessoa.FISICA)
                .endereco(null)
                .telefoneDTO("18996230715")
                .build();


        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .id(id)
                .conta(criarContaBuilder())
                .login("teste")
                .pessoaDTO(pessoaSalva)
                .build();

        UsuarioDTO usuarioDTOSalvo = criarUsuarioComumDTO();
        usuarioDTOSalvo.setConta(criarContaBuilder());
        usuarioDTOSalvo.setLogin("teste");
        usuarioDTOSalvo.setPessoaDTO(pessoaSalva);
        usuarioDTOSalvo.setId(id);

        BDDMockito.given(service.buscarUsuarioId(usuarioDTO.getId())).willReturn(Optional.of(Usuario.builder().id(id).build()));

        BDDMockito.given(service.alterar(usuarioDTO)).willReturn(usuarioDTOSalvo);

        BDDMockito.given(service.usuarioLogado()).willReturn(
                Usuario.builder().contaAdmin(true).id(1L).build());


        String json = new ObjectMapper().writeValueAsString(usuarioDTO);

        String URL_ALTERAR = URL.concat("/").concat(id.toString());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(URL_ALTERAR)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect( jsonPath("id").isNotEmpty())
                .andExpect( jsonPath("login").value(usuarioDTO.getLogin()))
                .andExpect( jsonPath("pessoaDTO.nomeRazaoSocial").value(usuarioDTO.getPessoaDTO().getNomeRazaoSocial()));

    }

    @Test
    @DisplayName("Não deve alterar um usuário sem Permissão." )
    void naoDeveAlterarUsuarioSemPermissao() throws Exception {
        Long id = 1L;

        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .id(id)
                .nomeRazaoSocial("teste")
                .build();

        BDDMockito.given(service.usuarioLogado()).willReturn(
                Usuario.builder().contaAdmin(false).pessoa(Pessoa.builder().id(0L).build()).build());

        String json = new ObjectMapper().writeValueAsString(usuarioDTO);

        String URL_ALTERAR = URL.concat("/").concat(id.toString());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(URL_ALTERAR)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usuario sem permissao"));
    }

    @Test
    @DisplayName("Não deve alterar um usuário sem Permissão." )
    void naoDeveAlterarUsuarioInexistente() throws Exception {
        Long id = 1L;

        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .id(id)
                .nomeRazaoSocial("teste")
                .build();

        BDDMockito.given(service.usuarioLogado()).willReturn(
                Usuario.builder().contaAdmin(true).id(1L).build());

        BDDMockito.given(service.buscarUsuarioId(usuarioDTO.getId())).willReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(usuarioDTO);

        String URL_ALTERAR = URL.concat("/").concat(id.toString());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(URL_ALTERAR)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario nao existe"));
    }

    @Test
    @DisplayName("Deve excluir um usuario existente.")
    void excluirUsuario() throws Exception {
        Long id = 1L;

        BDDMockito.given(service.usuarioLogado()).willReturn(
                Usuario.builder().contaAdmin(true).id(1L).build());

        BDDMockito.given(service.buscarUsuarioId(id)).willReturn(Optional.of(Usuario.builder().id(id).build()));


        String URL_EXCLUIR = URL.concat("/").concat(id.toString());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(URL_EXCLUIR);

        mvc
                .perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Não deve excluir um usuário sem Permissão.")
    void naoDeveExcluirUsuario() throws Exception {
        Long id = 1L;

        BDDMockito.given(service.usuarioLogado()).willReturn(
                Usuario.builder().contaAdmin(false).id(0L).build());

        String URL_EXCLUIR = URL.concat("/").concat(id.toString());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(URL_EXCLUIR);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Acesso Negado"));
    }

    private UsuarioDTO criarUsuarioComumDTO() {
        return UsuarioDTO.builder()
                .nomeRazaoSocial("Lucas Azevedo Souza")
                .login("50336912870")
                .cpfCnpj("50336912870")
                .senha("05/02/2001")
                .conta(null)
                .build();
    }

    private Conta criarContaBuilder() {
        return Conta.builder()
                .agencia(12)
                .numero(1234)
                .saldo(BigDecimal.valueOf(0))
                .build();
    }

}
