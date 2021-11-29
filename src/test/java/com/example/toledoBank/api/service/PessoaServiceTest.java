package com.example.toledoBank.api.service;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.enums.TipoPessoa;
import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.model.Pessoa;
import com.example.toledoBank.api.model.Telefone;
import com.example.toledoBank.api.repository.PessoaRepository;
import com.example.toledoBank.api.service.impl.PessoaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PessoaServiceTest {

    PessoaService pessoaService;

    @MockBean
    TelefoneService telefoneService;

    @MockBean
    EnderecoService enderecoService;

    @MockBean
    private PessoaRepository repository;



    @BeforeEach
    public void setUp(){
        this.pessoaService = new PessoaServiceImpl(this.repository, this.enderecoService, this.telefoneService);
    }

    @Test
    @DisplayName("Deve salvar uma pessoa.")
    void salvarPessoa() {

        Endereco endereco = criarEndereco();

        Telefone telefone = criarTelefone();

        PessoaDTO pessoaDTO = PessoaDTO.builder()
                .nomeRazaoSocial("Lucas Azevedo Souza")
                .cpfCnpj("50336912870")
                .dataNascAbertura(LocalDate.of(2001, 2, 5).toString())
                .tipoPessoa(TipoPessoa.FISICA)
                .endereco(endereco)
                .telefoneDTO("18996230715")
                .build();

        Pessoa pessoa = criarPessoaFisica(endereco, telefone);

        BDDMockito.given(telefoneService.save(Mockito.any(Telefone.class))).willReturn(telefone);
        BDDMockito.given(enderecoService.save(Mockito.any(Endereco.class))).willReturn(endereco);
        BDDMockito.given(repository.save(Mockito.any(Pessoa.class))).willReturn(pessoa);

        PessoaDTO pessoaSalva = pessoaService.salvar(pessoaDTO);

        assertThat(pessoaSalva.getId()).isNotNull();
        assertThat(pessoaSalva.getNomeRazaoSocial()).isEqualTo("Lucas Azevedo Souza");
        assertThat(pessoaSalva.getCpfCnpj()).isEqualTo("50336912870");
        assertThat(pessoaSalva.getDataNascAbertura()).isEqualTo(LocalDate.of(2001,2,5).toString());
        assertThat(pessoaSalva.getTipoPessoa()).isEqualTo(TipoPessoa.FISICA);
        assertThat(pessoaSalva.getEndereco()).isEqualTo(endereco);
        assertThat(pessoaSalva.getTelefoneDTO()).isEqualTo("18996230715");
    }


    @Test
    @DisplayName("Deve salvar o telefone que veio do DTO com sucesso.")
    void salvarTelefone() {

        Telefone telefoneSalvo = criarTelefone();

        Pessoa pessoa = Pessoa.builder()
                .telefone(Telefone.builder()
                        .numero("18996230715")
                        .build())
                .build();

        BDDMockito.given(telefoneService.save(Mockito.any(Telefone.class))).willReturn(telefoneSalvo);
        pessoa.setTelefone(pessoaService.salvarTelefone(pessoa.getTelefone().getNumero()));


        assertThat(pessoa.getTelefone().getId()).isNotNull();
        assertThat(pessoa.getTelefone().getNumero()).isEqualTo("18996230715");

    }

    @Test
    @DisplayName("Deve salvar o endereco que veio do DTO com sucesso.")
    void salvarEndereco() {
        Endereco endereco =  Endereco.builder()
                .rua("Rua Para")
                .numero(975)
                .cep(16015283)
                .bairro("Jardim Sumaré")
                .cidade("Araçatuba")
                .estado("São Paulo")
                .build();

        Endereco enderecoSalvo = criarEndereco();

        Pessoa pessoa = Pessoa.builder()
                .endereco(endereco)
                .build();

        BDDMockito.given(enderecoService.save(Mockito.any(Endereco.class))).willReturn(enderecoSalvo);
        pessoa.setEndereco(pessoaService.salvarEndereco(pessoa.getEndereco()));

        assertThat(pessoa.getEndereco().getId()).isNotNull();
        assertThat(pessoa.getEndereco().getBairro()).isEqualTo("Jardim Sumaré");
        assertThat(pessoa.getEndereco().getRua()).isEqualTo("Rua Para");
        assertThat(pessoa.getEndereco().getNumero()).isEqualTo(975);
        assertThat(pessoa.getEndereco().getCep()).isEqualTo(16015283);
        assertThat(pessoa.getEndereco().getCidade()).isEqualTo("Araçatuba");
        assertThat(pessoa.getEndereco().getEstado()).isEqualTo("São Paulo");

    }

    @Test
    @DisplayName("Deve retornar uma pessoa com o CPF cadastrado")
    void buscarPessoaCpf() {
        Pessoa pessoa = criarPessoaFisica(criarEndereco(), criarTelefone());

        BDDMockito.given(repository.findByCpfCnpj(Mockito.any(String.class))).willReturn(pessoa);

        Pessoa pessoaBanco = pessoaService.buscarPorCPF("50336912870");

        assertThat(pessoaBanco.getId()).isNotNull();
        assertThat(pessoaBanco.getCpfCnpj()).isEqualTo("50336912870");
        assertThat(pessoaBanco.getNomeRazaoSocial()).isEqualTo("Lucas Azevedo Souza");
        assertThat(pessoaBanco.getDataNascAbertura()).isEqualTo(LocalDate.of(2001, 2, 5));
        assertThat(pessoaBanco.getTipoPessoa()).isEqualTo(TipoPessoa.FISICA);

    }

    @Test
    @DisplayName("Deve retornar true para uma pessoa com o CPF cadastrado")
    void existePessoaCpf() {
        Pessoa pessoa = criarPessoaFisica(criarEndereco(), criarTelefone());

        BDDMockito.given(repository.existsByCpfCnpj(Mockito.any(String.class))).willReturn(true);

        Boolean existePessoa = pessoaService.existePorCPF(pessoa.getCpfCnpj());

        assertThat(existePessoa).isTrue();


    }

    private Pessoa criarPessoaFisica(Endereco endereco, Telefone telefone) {
        return Pessoa.builder()
                .id(1L)
                .nomeRazaoSocial("Lucas Azevedo Souza")
                .cpfCnpj("50336912870")
                .dataNascAbertura(LocalDate.of(2001, 2, 5))
                .tipoPessoa(TipoPessoa.FISICA)
                .endereco(endereco)
                .telefone(telefone)
                .build();
    }

    private Telefone criarTelefone() {
        return Telefone.builder()
                .id(1L)
                .numero("18996230715")
                .build();
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
