package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.enums.TipoPessoa;
import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.seguranca.JwtAuthenticationEntryPoint;
import com.example.toledoBank.api.service.PessoaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        BDDMockito.given(service.save(pessoaDTO)).willReturn(pessoaSalva);

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
