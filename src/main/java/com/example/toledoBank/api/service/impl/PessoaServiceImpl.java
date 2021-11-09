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
    public PessoaDTO save(PessoaDTO pessoaDTO) {

        Pessoa pessoa = modelMapper.map(pessoaDTO, Pessoa.class);
        pessoa.setTelefone(this.salvarTelefone(pessoaDTO.getTelefoneDTO()));
        pessoa.setDataNascAbertura(LocalDate.parse(pessoaDTO.getDataNascAbertura()));

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
    public Pessoa buscarPorCPF(String cpf) {
        return pessoaRepository.findByCpfCnpj(cpf);
    }

    @Override
    public Boolean existePorCPF(String cpf) {
        return pessoaRepository.existsByCpfCnpj(cpf);
    }
}
