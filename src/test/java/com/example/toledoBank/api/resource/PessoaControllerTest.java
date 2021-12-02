package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.enums.TipoPessoa;
import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.model.Pessoa;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.seguranca.JwtAuthenticationEntryPoint;
import com.example.toledoBank.api.service.PessoaService;
import com.example.toledoBank.api.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false) // coloco isso pra não fazer a autenticação
@ContextConfiguration(classes = {JwtAuthenticationEntryPoint.class}) // Carrego esse cara para fazer o EntryPoint
@Import({ModelMapper.class, PessoaController.class}) // o Import no ModelMapper é explicável.. o Import do UsuarioController tá estranho.. não deveria ser assim..
public class PessoaControllerTest {

    static String URL = "/pessoa";

    @Autowired
    MockMvc mvc;

    @MockBean
    PessoaService service;

    @MockBean
    UsuarioService usuarioService;


    @Test
    @DisplayName("Deve criar uma pessoa com sucesso.")
    void criarPessoa() throws Exception {
        Endereco endereco = criarEndereco();

        PessoaDTO pessoaDTO = criarPessoaDTO(endereco);

        PessoaDTO pessoaSalva = PessoaDTO.builder()
                .id(1L)
                .nomeRazaoSocial("Lucas Azevedo Souza")
                .cpfCnpj("50336912870")
                .dataNascAbertura(LocalDate.of(2001, 2, 5).toString())
                .tipoPessoa(TipoPessoa.FISICA)
                .endereco(endereco)
                .telefoneDTO("18996230715")
                .build();

        BDDMockito.given(service.salvar(pessoaDTO)).willReturn(pessoaSalva);

        String json = new ObjectMapper().writeValueAsString(pessoaDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect( jsonPath("id").isNotEmpty())
                .andExpect( jsonPath("nomeRazaoSocial").value(pessoaDTO.getNomeRazaoSocial()))
                .andExpect( jsonPath("cpfCnpj").value(pessoaDTO.getCpfCnpj()))
                .andExpect( jsonPath("dataNascAbertura").value(pessoaDTO.getDataNascAbertura()))
                .andExpect( jsonPath("tipoPessoa").value(pessoaDTO.getTipoPessoa().toString()))
                .andExpect( jsonPath("endereco").value(pessoaDTO.getEndereco()))
                .andExpect( jsonPath("telefoneDTO").value("18996230715"));
    }

    @Test
    @DisplayName("Deve Alterar uma pessoa com sucesso.")
    void alterarPessoa() throws Exception {
        Long id = 1L;

        Endereco endereco = criarEndereco();
        endereco.setNumero(22);

        PessoaDTO pessoaDTO = PessoaDTO.builder()
                .nomeRazaoSocial("Teste")
                .endereco(endereco)
                .build();


        PessoaDTO pessoaSalva = PessoaDTO.builder()
                .id(id)
                .nomeRazaoSocial("Teste")
                .cpfCnpj("50336912870")
                .dataNascAbertura(LocalDate.of(2001, 2, 5).toString())
                .tipoPessoa(TipoPessoa.FISICA)
                .endereco(endereco)
                .telefoneDTO("18996230715")
                .build();

        BDDMockito.given(service.buscarPessoaId(BDDMockito.anyLong())).willReturn(Optional.of(Pessoa.builder().id(1L).build()));

        BDDMockito.given(service.alterar(BDDMockito.any(PessoaDTO.class))).willReturn(pessoaSalva);

        BDDMockito.given(usuarioService.usuarioLogado()).willReturn(
                Usuario.builder().contaAdmin(true).pessoa(Pessoa.builder().id(1L).build()).build());


        String json = new ObjectMapper().writeValueAsString(pessoaDTO);

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
                .andExpect( jsonPath("nomeRazaoSocial").value(pessoaDTO.getNomeRazaoSocial()))
                .andExpect( jsonPath("endereco").value(pessoaDTO.getEndereco()));

    }

    @Test
    @DisplayName("Não deve alterar uma pessoa sem Permissão." )
    void erroAlterarPessoaSemPermissao() throws Exception {
        Long id = 1L;

        PessoaDTO pessoaDTO = PessoaDTO.builder()
                .id(id)
                .endereco(null)
                .tipoPessoa(TipoPessoa.FISICA)
                .nomeRazaoSocial("teste")
                .build();

        BDDMockito.given(usuarioService.usuarioLogado()).willReturn(
                Usuario.builder().contaAdmin(false).pessoa(Pessoa.builder().id(0L).build()).build());

        String json = new ObjectMapper().writeValueAsString(pessoaDTO);

        String URL_ALTERAR_ERROR = URL.concat("/").concat(id.toString());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(URL_ALTERAR_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Acesso negado"));
    }

    @Test
    @DisplayName("Não deve alterar uma pessoa sem Existir." )
    void erroAlterarPessoaSemExistir() throws Exception {
        Long id = 1L;

        PessoaDTO pessoaDTO = PessoaDTO.builder()
                .id(id)
                .endereco(null)
                .tipoPessoa(TipoPessoa.FISICA)
                .nomeRazaoSocial("teste")
                .build();

        BDDMockito.given(usuarioService.usuarioLogado()).willReturn(
                Usuario.builder().contaAdmin(true).pessoa(Pessoa.builder().id(0L).build()).build());

        BDDMockito.given(service.buscarPessoaId(pessoaDTO.getId())).willReturn(Optional.empty());


        String json = new ObjectMapper().writeValueAsString(pessoaDTO);

        String URL_ALTERAR_ERROR = URL.concat("/").concat(id.toString());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(URL_ALTERAR_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pessoa inexistente"));
    }

    @Test
    @DisplayName("Deve excluir um pessoa existente.")
    void excluirPessoa() throws Exception {
        Long id = 1L;

//        BDDMockito.when(service.buscarPessoaId(BDDMockito.anyLong())).thenReturn(Optional.of(Pessoa.builder().id(id).nomeRazaoSocial("teste").build()));

        BDDMockito.given(usuarioService.usuarioLogado()).willReturn(
                Usuario.builder().contaAdmin(true).pessoa(Pessoa.builder().id(0L).build()).build());


        String URL_EXCLUIR = URL.concat("/").concat(id.toString());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(URL_EXCLUIR);

        mvc
                .perform(request)
                .andExpect(status().isOk());
    }



    private Endereco criarEndereco() {
        return Endereco.builder()
                .id(1L)
                .rua("Rua Para")
                .numero(975)
                .cep(16015283)
                .bairro("Jardim Sumaré")
                .cidade("Araçatuba")
                .estado("São Paulo")
                .build();
    }


    private PessoaDTO criarPessoaDTO(Endereco endereco) {
        return PessoaDTO.builder()
                .nomeRazaoSocial("Lucas Azevedo Souza")
                .cpfCnpj("50336912870")
                .dataNascAbertura(LocalDate.of(2001, 2, 5).toString())
                .tipoPessoa(TipoPessoa.FISICA)
                .endereco(endereco)
                .telefoneDTO("18996230715")
                .build();
    }

}
