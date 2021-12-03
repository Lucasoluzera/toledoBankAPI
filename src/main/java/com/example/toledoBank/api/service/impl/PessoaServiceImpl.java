package com.example.toledoBank.api.service.impl;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.model.Pessoa;
import com.example.toledoBank.api.model.Telefone;
import com.example.toledoBank.api.repository.PessoaRepository;
import com.example.toledoBank.api.service.EnderecoService;
import com.example.toledoBank.api.service.PessoaService;
import com.example.toledoBank.api.service.TelefoneService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaServiceImpl implements PessoaService {


    @Autowired
    private final ModelMapper modelMapper = new ModelMapper();

    private PessoaRepository pessoaRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private TelefoneService telefoneService;

    public PessoaServiceImpl(PessoaRepository pessoaRepository, EnderecoService enderecoService, TelefoneService telefoneService) {
        this.pessoaRepository = pessoaRepository;
        this.enderecoService = enderecoService;
        this.telefoneService = telefoneService;
    }

    @Override
    public PessoaDTO salvar(PessoaDTO pessoaDTO) {

        Pessoa pessoa = modelMapper.map(pessoaDTO, Pessoa.class);
        pessoa.setTelefone(this.salvarTelefone(pessoaDTO.getTelefoneDTO()));

        if(pessoa.getDataNascAbertura() != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            pessoa.setDataNascAbertura(LocalDate.parse(pessoaDTO.getDataNascAbertura()));
        }

        pessoa.setEndereco(salvarEndereco(pessoa.getEndereco()));

        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        PessoaDTO pessoaDTOSalvo = modelMapper.map(pessoaSalva, PessoaDTO.class);
        pessoaDTOSalvo.setTelefoneDTO(pessoa.getTelefone().getNumero());

        return pessoaDTOSalvo;
    }

    @Override
    public Telefone salvarTelefone(String numero) {
        Telefone telefone = Telefone.builder()
                .numero(numero)
                .build();
        return telefoneService.save(telefone);
    }

    @Override
    public Endereco salvarEndereco(Endereco endereco) {
        return enderecoService.save(endereco);
    }

    @Override
    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    @Override
    public Pessoa buscarPorCPF(String cpf) {
        return pessoaRepository.findByCpfCnpj(cpf);
    }

    @Override
    public Boolean existePorCPF(String cpf) {
        return pessoaRepository.existsByCpfCnpj(cpf);
    }

    @Override
    public PessoaDTO alterar(PessoaDTO pessoaDTO) {
        Pessoa pessoa = modelMapper.map(pessoaDTO, Pessoa.class);

        if(pessoaRepository.findById(pessoaDTO.getId()).isPresent()){
            Pessoa pessoaBanco =  pessoaRepository.findById(pessoaDTO.getId()).get();

            Telefone telefone =
                    Telefone.builder()
                    .id(pessoaBanco.getTelefone().getId())
                    .numero(pessoaDTO.getTelefoneDTO())
                    .build();

            Endereco endereco = pessoa.getEndereco();
            endereco.setId(pessoaBanco.getEndereco().getId());

            this.enderecoService.update(endereco);
            this.telefoneService.update(telefone);

            pessoa.setTelefone(telefone);
            pessoa.setEndereco(endereco);

            pessoa = this.pessoaRepository.save(pessoa);

            pessoaDTO = modelMapper.map(pessoa, PessoaDTO.class);

            pessoaDTO.setTelefoneDTO(pessoa.getTelefone().getNumero());

            return pessoaDTO;
        }
        throw new RuntimeException("Id não encontrado");
    }

    @Override
    public void excluir(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id).get();
        pessoaRepository.deleteById(id);

        if(pessoa.getTelefone() != null)
            this.telefoneService.excluir(pessoa.getTelefone().getId());

        if(pessoa.getEndereco() != null)
            this.enderecoService.excluir(pessoa.getEndereco().getId());
    }

    @Override
    public Optional<Pessoa> buscarPessoaId(Long id) {
        return this.pessoaRepository.findById(id);
    }

}
