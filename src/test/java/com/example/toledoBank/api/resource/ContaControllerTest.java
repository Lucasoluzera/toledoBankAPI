package com.example.toledoBank.api.resource;


import com.example.toledoBank.api.dto.ContaOperacoesDTO;
import com.example.toledoBank.api.model.Conta;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.seguranca.JwtAuthenticationEntryPoint;
import com.example.toledoBank.api.service.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {JwtAuthenticationEntryPoint.class})
@Import({ContaController.class})
public class ContaControllerTest {

    static String URL = "/conta";

    @Autowired
    MockMvc mvc;

    @MockBean
    ContaService service;

    @Test
    @DisplayName("Deve sacar valor com sucesso.")
    void sacarValorConta() throws Exception {

        ContaOperacoesDTO contaOperacoesDTO = criarContaOperacoesDTO();


        ContaOperacoesDTO contaOperacoesDTOSalvo = criarContaOperacoesDTO();

        contaOperacoesDTOSalvo.getUsuario().getConta().setId(1L);
        contaOperacoesDTOSalvo.getUsuario().getConta().setSaldo(
                contaOperacoesDTOSalvo.getUsuario().getConta().getSaldo().subtract(
                        BigDecimal.valueOf(Double.parseDouble(contaOperacoesDTO.getSaldo()))));

        BDDMockito.given(service.sacar(Mockito.any(ContaOperacoesDTO.class))).willReturn(contaOperacoesDTOSalvo);
        String URLSacar = URL.concat("/sacar");
        String json = new ObjectMapper().writeValueAsString(contaOperacoesDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URLSacar)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("usuario.conta.id").isNotEmpty())
                .andExpect(jsonPath("usuario.conta.agencia").value(12))
                .andExpect(jsonPath("usuario.conta.numero").value(1234))
                .andExpect(jsonPath("usuario.conta.saldo").value(BigDecimal.valueOf(0.05)))
                .andExpect(jsonPath("cpfContaSecundaria").value("1111111111"))
                .andExpect(jsonPath("saldo").value("10.20"));

    }


    @Test
    @DisplayName("Deve depositar valor com sucesso.")
    void depositarValor() throws Exception {

        ContaOperacoesDTO contaOperacoesDTO = criarContaOperacoesDTO();

        ContaOperacoesDTO contaOperacoesDTOSalvo = criarContaOperacoesDTO();

        contaOperacoesDTOSalvo.getUsuario().getConta().setId(1L);
        contaOperacoesDTOSalvo.getUsuario().getConta().setSaldo(
                contaOperacoesDTOSalvo.getUsuario().getConta().getSaldo().add(
                        BigDecimal.valueOf(Double.parseDouble(contaOperacoesDTO.getSaldo()))));

        BDDMockito.given(service.depositar(Mockito.any(ContaOperacoesDTO.class))).willReturn(contaOperacoesDTOSalvo);

        String json = new ObjectMapper().writeValueAsString(contaOperacoesDTO);
        String URLdepositar = URL.concat("/depositar");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URLdepositar)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("usuario.conta.id").isNotEmpty())
                .andExpect(jsonPath("usuario.conta.agencia").value(12))
                .andExpect(jsonPath("usuario.conta.numero").value(1234))
                .andExpect(jsonPath("usuario.conta.saldo").value(BigDecimal.valueOf(20.45)))
                .andExpect(jsonPath("cpfContaSecundaria").value("1111111111"))
                .andExpect(jsonPath("saldo").value("10.20"));

    }


    @Test
    @DisplayName("Deve Transferir um valor com sucesso.")
    void transferirValor() throws Exception {

        ContaOperacoesDTO contaOperacoesDTO = criarContaOperacoesDTO();

        Conta contaSalva = criarContaBuilder();
        contaSalva.setId(1L);
        contaSalva.setSaldo(contaSalva.getSaldo().subtract(BigDecimal.valueOf(Double.parseDouble(contaOperacoesDTO.getSaldo()))));
        contaOperacoesDTO.getUsuario().setConta(contaSalva);

        BDDMockito.given(service.transferir(Mockito.any(ContaOperacoesDTO.class))).willReturn(contaOperacoesDTO);


        String json = new ObjectMapper().writeValueAsString(contaOperacoesDTO);
        String URLTransferir = URL.concat("/transferir");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URLTransferir)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("usuario.conta.id").isNotEmpty())
                .andExpect(jsonPath("usuario.conta.agencia").value(12))
                .andExpect(jsonPath("usuario.conta.numero").value(1234))
                .andExpect(jsonPath("usuario.conta.saldo").value(BigDecimal.valueOf(0.05)))
                .andExpect(jsonPath("cpfContaSecundaria").value("1111111111"))
                .andExpect(jsonPath("saldo").value("10.20"));

    }

    private ContaOperacoesDTO criarContaOperacoesDTO() {
        return ContaOperacoesDTO.builder()
                .usuario(criarUsuario())
                .cpfContaSecundaria("1111111111")
                .saldo("10.20")
                .build();
    }


    private Conta criarContaBuilder() {
        return Conta.builder()
                .agencia(12)
                .numero(1234)
                .saldo(BigDecimal.valueOf(10.25))
                .build();
    }

    private Usuario criarUsuario() {
        return Usuario.builder()
                .login("50336912870")
                .senha("05/02/2001")
                .conta(criarContaBuilder())
                .build();
    }

}
