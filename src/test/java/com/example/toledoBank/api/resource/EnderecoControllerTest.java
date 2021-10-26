package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.seguranca.JwtAuthenticationEntryPoint;
import com.example.toledoBank.api.service.EnderecoService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false) // coloco isso pra não fazer a autenticação
@ContextConfiguration(classes = {JwtAuthenticationEntryPoint.class}) // Carrego esse cara para fazer o EntryPoint
@Import({ModelMapper.class, EnderecoController.class}) // o Import no ModelMapper é explicável.. o Import do UsuarioController tá estranho.. não deveria ser assim..
public class EnderecoControllerTest {

    static String URL = "/endereco";

    @Autowired
    MockMvc mvc;

    @MockBean
    EnderecoService service;

    @Test
    @DisplayName("Deve Criar um Endereco com sucesso.")
    void criarEndereco() throws Exception {
        Endereco endereco = criarEnderecoObj();
        String json = new ObjectMapper().writeValueAsString(endereco);

        endereco.setId(1L);

        BDDMockito.given(service.save(Mockito.any(Endereco.class))).willReturn(endereco);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("rua").value(endereco.getRua()))
                .andExpect(jsonPath("numero").value(endereco.getNumero()))
                .andExpect(jsonPath("cep").value(endereco.getCep()))
                .andExpect(jsonPath("bairro").value(endereco.getBairro()))
                .andExpect(jsonPath("cidade").value(endereco.getCidade()))
                .andExpect(jsonPath("estado").value(endereco.getEstado()));
    }

    @Test
    @DisplayName("Deve alterar um Endereco com sucesso")
    void alterarEndereco() throws Exception {
        Long id = 1L;
        String json = new ObjectMapper().writeValueAsString(criarEnderecoObj());

        Endereco enderecoAtualizado = Endereco.builder()
                .id(id)
                .rua("Afonso Pena")
                .numero(2000)
                .cep(16015283)
                .bairro("Jardim Sumaré")
                .cidade("Araçatuba")
                .estado("São Paulo")
                .build();

        BDDMockito.given(service.getById(id))
                .willReturn(Optional.of(enderecoAtualizado));

        Endereco enderecoAtualizar = criarEnderecoObj();
        enderecoAtualizar.setId(id);

        BDDMockito.given(service.update(enderecoAtualizar)).willReturn(enderecoAtualizar);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(URL.concat("/" + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("rua").value(criarEnderecoObj().getRua()))
                .andExpect(jsonPath("numero").value(criarEnderecoObj().getNumero()))
                .andExpect(jsonPath("cep").value(criarEnderecoObj().getCep()))
                .andExpect(jsonPath("bairro").value(criarEnderecoObj().getBairro()))
                .andExpect(jsonPath("cidade").value(criarEnderecoObj().getCidade()))
                .andExpect(jsonPath("estado").value(criarEnderecoObj().getEstado()));
    }

    private Endereco criarEnderecoObj() {
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
